package com.deart.sistema_de_ponto_back.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.deart.sistema_de_ponto_back.dtos.requests.UserCreateRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.UserLoginRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.UserUpdatePasswordRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.UserUpdateRequest;
import com.deart.sistema_de_ponto_back.dtos.responses.LoginResponse;
import com.deart.sistema_de_ponto_back.dtos.responses.UserResponse;
import com.deart.sistema_de_ponto_back.exceptions.domain.DepartmentNotFoundException;
import com.deart.sistema_de_ponto_back.exceptions.domain.InvalidCredentialsException;
import com.deart.sistema_de_ponto_back.exceptions.domain.InvalidPasswordException;
import com.deart.sistema_de_ponto_back.exceptions.domain.UserAlreadyActiveException;
import com.deart.sistema_de_ponto_back.exceptions.domain.UserAlreadyInactiveException;
import com.deart.sistema_de_ponto_back.exceptions.domain.UserCpfAlreadyExistsException;
import com.deart.sistema_de_ponto_back.exceptions.domain.UserEmailAlreadyExistsException;
import com.deart.sistema_de_ponto_back.exceptions.domain.UserNotFoundException;
import com.deart.sistema_de_ponto_back.mappers.UserMapper;
import com.deart.sistema_de_ponto_back.models.Department;
import com.deart.sistema_de_ponto_back.models.User;
import com.deart.sistema_de_ponto_back.repositories.DepartmentRepository;
import com.deart.sistema_de_ponto_back.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final UserMapper mapper;

    public UserService(DepartmentRepository departmentRepository, UserRepository userRepository, UserMapper mapper){
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public List<UserResponse> findAll(){
        return userRepository.findAll()
            .stream().map(mapper::toResponse)
            .toList();
    }

    public List<UserResponse> findAllActives(){
        return userRepository.findAllByActiveTrue()
            .stream().map(mapper::toResponse)
            .toList();  
    }

    // adicionar verificação se está no local presencialmente
    public LoginResponse login(UserLoginRequest loginRequest){
        User user = userRepository.findByCpf(loginRequest.cpf())
            .orElseThrow(UserNotFoundException::new);

        // encriptar antes de comparar
        if ( !(user.getPassword().equals(loginRequest.password()))) {
            throw new InvalidCredentialsException();
        }

        // String token = tokenService.generateToken(user);
        // Long expiresIn = tokenService.getExpirationTime(); 

        return mapper.toLoginResponse(null, null, null);
    }

    // Verificar ROLE
    @Transactional
    public UserResponse create(UserCreateRequest createRequest){
        if (userRepository.existsByCpf(createRequest.cpf())) {
            throw new UserCpfAlreadyExistsException(createRequest.cpf());
        }
        if (userRepository.existsByEmail(createRequest.email())) {
            throw new UserEmailAlreadyExistsException(createRequest.email());
        }
        Department department = departmentRepository.findByExternalId(createRequest.departmentExternalId())
            .orElseThrow(DepartmentNotFoundException::new);

        User user = mapper.toEntity(createRequest);
        user.setDepartment(department);
        user.setPassword("senha_padrao");     //ENCRIPTAR
        
        return mapper.toResponse(userRepository.save(user));
    }

    // verificar ROLE (bolsista não pode mudar seu ROLE)
    @Transactional
    public UserResponse update(UUID externalId, UserUpdateRequest updateRequest){
        User user = userRepository.findByExternalIdAndActiveTrue(externalId)
                .orElseThrow(UserNotFoundException::new);

        if (userRepository.existsByCpfAndExternalIdNot(updateRequest.cpf(), externalId)) {
            throw new UserCpfAlreadyExistsException(updateRequest.cpf());
        }
        if (userRepository.existsByEmailAndExternalIdNot(updateRequest.email(), externalId)) {
            throw new UserEmailAlreadyExistsException(updateRequest.email());
        }

        mapper.updateEntityFromRequest(updateRequest, user);

        if (updateRequest.departmentExternalId() != null) {
            Department dept = departmentRepository.findByExternalId(updateRequest.departmentExternalId())
                    .orElseThrow(DepartmentNotFoundException::new);
            user.setDepartment(dept);
        }

        return mapper.toResponse(userRepository.save(user));
    }

    // adicionar +1 passo de segurança
    // ativação por email, código por email, etc...
    @Transactional
    public void updateEmail(UUID externalId, String newEmail) {
        User user = userRepository.findByExternalId(externalId)
                .orElseThrow(UserNotFoundException::new);

        if (userRepository.existsByEmailAndExternalIdNot(newEmail, externalId)) {
            throw new UserEmailAlreadyExistsException(newEmail);
        }

        user.setEmail(newEmail);
        userRepository.save(user);
    }

    // adicionar  +1 passo de segurança
    // deve estar logado e ser o usuário ou o ADMIN
    @Transactional
    public void updatePassword(UUID externalId, UserUpdatePasswordRequest request) {
        User user = userRepository.findByExternalIdAndActiveTrue(externalId)
                .orElseThrow(UserNotFoundException::new);

        // encriptar antes de comparar
        if (request.currentPassword().equals(user.getPassword())) {
            throw new InvalidPasswordException();
        }

        user.setPassword(request.newPassword());    // ENCRIPTAR
        userRepository.save(user);
    }

    @Transactional
    public void updateCpf(UUID externalId, String newCpf) {
        User user = userRepository.findByExternalIdAndActiveTrue(externalId)
                .orElseThrow(UserNotFoundException::new);

        if (userRepository.existsByCpfAndExternalIdNot(newCpf, externalId)) {
            throw new UserCpfAlreadyExistsException(newCpf);
        }

        user.setCpf(newCpf); 
        userRepository.save(user);
    }

    // verificar ROLE
    @Transactional
    public UserResponse deactivate(UUID externalId){
        User user = userRepository.findByExternalId(externalId).orElseThrow(UserNotFoundException::new);

        if (!user.getActive()) throw new UserAlreadyInactiveException();
        
        user.setActive(false);
        return mapper.toResponse(userRepository.save(user));
    }

    // verificar ROLE
    @Transactional
    public UserResponse activate(UUID externalId){
        User user = userRepository.findByExternalId(externalId).orElseThrow(UserNotFoundException::new);

        if (user.getActive()) throw new UserAlreadyActiveException();

        user.setActive(true);
        return mapper.toResponse(userRepository.save(user));
    }
}
