package com.deart.sistema_de_ponto_back.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.deart.sistema_de_ponto_back.dtos.requests.UserCreateRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.UserLoginRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.UserUpdatePasswordRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.UserUpdateRequest;
import com.deart.sistema_de_ponto_back.exceptions.domain.InactiveUserException;
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
import com.deart.sistema_de_ponto_back.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    private final DepartmentService departmentService;
    private final UserRepository userRepository;
    private final UserMapper mapper;

    public UserService(DepartmentService departmentService, UserRepository userRepository, UserMapper mapper){
        this.departmentService = departmentService;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public String cleanCpf(String cpf) {
        if (cpf == null) return null;
        return cpf.replaceAll("\\D", "");
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public List<User> findAllActives(){
        return userRepository.findAllByActiveTrue();  
    }

    public User findByExternalId(UUID externalId) {
        return userRepository.findByExternalId(externalId)
                .orElseThrow(UserNotFoundException::new);
    }

    public Optional<User> findByCpf(String cpf) {
        return userRepository.findByCpf(cleanCpf(cpf));
    }

    public void validateUserActive(User user, String role){
        if (!user.getActive()) {
            if (role == null || role.isBlank()) {
                throw new InactiveUserException(); 
            }
            throw new InactiveUserException(role);
        }
    }

    // adicionar verificação se está no local presencialmente
    public User login(UserLoginRequest loginRequest){
        String cleanCpf = cleanCpf(loginRequest.cpf());
        User user = userRepository.findByCpf(cleanCpf)
            .orElseThrow(InvalidCredentialsException::new);

        // encriptar antes de comparar
        if (!(user.getPassword().equals(loginRequest.password()))) {
            throw new InvalidCredentialsException();
        }

        if (!user.getActive()) {
            throw new InvalidCredentialsException(); 
        }

        return user;
    }

    // Verificar ROLE
    @Transactional
    public User create(UserCreateRequest createRequest){
        String cleanCpf = cleanCpf(createRequest.cpf());
        if (userRepository.existsByCpf(cleanCpf)) {
            throw new UserCpfAlreadyExistsException(cleanCpf);
        }
        if (userRepository.existsByEmail(createRequest.email())) {
            throw new UserEmailAlreadyExistsException(createRequest.email());
        }
        Department department = departmentService.findByExternalId(createRequest.departmentExternalId());

        User user = mapper.toEntity(createRequest);
        user.setDepartment(department);
        user.setPassword("senha_padrao");     //ENCRIPTAR
        
        return userRepository.save(user);
    }

    // verificar ROLE (bolsista não pode mudar seu ROLE)
    @Transactional
    public User update(UUID externalId, UserUpdateRequest updateRequest){
        User user = userRepository.findByExternalId(externalId)
                .orElseThrow(UserNotFoundException::new);

        validateUserActive(user, "usuário");

        String cleanUpdateCpf = cleanCpf(updateRequest.cpf());

        if (!cleanUpdateCpf.equals(user.getCpf())) {
            if (userRepository.existsByCpf(cleanUpdateCpf)) {
                throw new UserCpfAlreadyExistsException(cleanUpdateCpf);
            }
        }

        if (!updateRequest.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updateRequest.email())) {
                throw new UserEmailAlreadyExistsException(updateRequest.email());
            }
        }

        mapper.updateEntityFromRequest(updateRequest, user);

        UUID newDeptId = updateRequest.departmentExternalId();
        UUID currentDeptId = (user.getDepartment() != null) ? user.getDepartment().getExternalId() : null;

        if (newDeptId != null) {
            if (!newDeptId.equals(currentDeptId)) {
                Department dept = departmentService.findByExternalId(newDeptId);
                user.setDepartment(dept);
            }
        } else {
            user.setDepartment(null);
        }

        return userRepository.save(user);
    }

    // adicionar +1 passo de segurança
    // ativação por email, código por email, etc...
    // mudar retorno
    @Transactional
    public void updateEmail(UUID externalId, String newEmail) {
        User user = userRepository.findByExternalId(externalId)
                .orElseThrow(UserNotFoundException::new);

        validateUserActive(user, "usuário");

        if (userRepository.existsByEmailAndExternalIdNot(newEmail, externalId)) {
            throw new UserEmailAlreadyExistsException(newEmail);
        }

        user.setEmail(newEmail);
        userRepository.save(user);
    }

    // adicionar  +1 passo de segurança
    // deve estar logado e ser o usuário ou o ADMIN
    // mudar retorno
    @Transactional
    public void updatePassword(UUID externalId, UserUpdatePasswordRequest request) {
        User user = userRepository.findByExternalId(externalId)
                .orElseThrow(UserNotFoundException::new);

        validateUserActive(user, "usuário");

        // encriptar antes de comparar
        if (!request.currentPassword().equals(user.getPassword())) {
            throw new InvalidPasswordException();
        }

        user.setPassword(request.newPassword());    // ENCRIPTAR
        userRepository.save(user);
    }

    // mudar retorno
    @Transactional
    public void updateCpf(UUID externalId, String newCpf) {
        User user = userRepository.findByExternalId(externalId)
                .orElseThrow(UserNotFoundException::new);

        validateUserActive(user, "usuário");

        String cleanCpf = cleanCpf(newCpf);

        if (userRepository.existsByCpfAndExternalIdNot(cleanCpf, externalId)) {
            throw new UserCpfAlreadyExistsException(cleanCpf);
        }

        user.setCpf(newCpf); 
        userRepository.save(user);
    }

    // verificar ROLE
    @Transactional
    public User deactivate(UUID externalId){
        User user = userRepository.findByExternalId(externalId).orElseThrow(UserNotFoundException::new);

        if (!user.getActive()) throw new UserAlreadyInactiveException();
        
        user.setActive(false);
        return userRepository.save(user);
    }

    // verificar ROLE
    @Transactional
    public User activate(UUID externalId){
        User user = userRepository.findByExternalId(externalId).orElseThrow(UserNotFoundException::new);

        if (user.getActive()) throw new UserAlreadyActiveException();

        user.setActive(true);
        return userRepository.save(user);
    }
}
