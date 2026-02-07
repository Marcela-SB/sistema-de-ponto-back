package com.deart.sistema_de_ponto_back.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.deart.sistema_de_ponto_back.dtos.requests.InternCreateRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.InternUpdateRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.UserCreateRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.UserUpdateRequest;
import com.deart.sistema_de_ponto_back.enums.UserRole;
import com.deart.sistema_de_ponto_back.exceptions.domain.InternAlreadyExistsException;
import com.deart.sistema_de_ponto_back.exceptions.domain.InternEnrollmentNumberAlreadyExistsException;
import com.deart.sistema_de_ponto_back.exceptions.domain.InternNotFoundException;
import com.deart.sistema_de_ponto_back.exceptions.domain.InvalidUserRoleException;
import com.deart.sistema_de_ponto_back.mappers.InternMapper;
import com.deart.sistema_de_ponto_back.models.Intern;
import com.deart.sistema_de_ponto_back.models.User;
import com.deart.sistema_de_ponto_back.repositories.InternRepository;

import jakarta.transaction.Transactional;

@Service
public class InternService {
    private final InternRepository internRepository;
    private final UserService userService;
    private final InternMapper internMapper;
    
    public InternService(InternRepository internRepository, UserService userService, InternMapper internMapper){
        this.internRepository = internRepository;
        this.userService = userService;
        this.internMapper = internMapper;
    }

    public List<Intern> findAll(){
        return internRepository.findAll();
    }

    public List<Intern> findAllActives(){
        return internRepository.findAllByUserActiveTrue();
    }

    public List<Intern> findAllBySupervisor(UUID supervisorExternalId){
        return internRepository.findAllBySupervisorExternalId(supervisorExternalId);
    }

    private User validateAndGetSupervisor(UUID externalId){
        User supervisor = userService.findByExternalId(externalId);
        if (supervisor.getRole() != UserRole.SUPERVISOR && supervisor.getRole() != UserRole.ADMIN) {
            throw new InvalidUserRoleException("Supervisor ou Administrador");
        }
        userService.validateUserActive(supervisor, "supervisor");

        return supervisor;
    }

    @Transactional
    public Intern create(InternCreateRequest createRequest){
        UserCreateRequest userData = createRequest.user();

        User supervisor = validateAndGetSupervisor(createRequest.supervisorExternalId());

        if (internRepository.existsByEnrollmentNumber(createRequest.enrollmentNumber())) {
            throw new InternEnrollmentNumberAlreadyExistsException(createRequest.enrollmentNumber());
        }

        User user = userService.findByCpf(userData.cpf())
            .map(existingUser -> {
                // CENÁRIO 1: O usuário já existe, validamos se está ativo
                userService.validateUserActive(existingUser, null);
                return existingUser;
            }).orElseGet(() ->{
                // CENÁRIO 2: O usuário não existe, criamos um novo
                return userService.create(userData);
            });
        
        if (internRepository.existsById(user.getId())) {
            throw new InternAlreadyExistsException();
        }
        
        user.setRole(UserRole.INTERN);
        Intern intern = internMapper.toEntityFromCreate(createRequest, user);
        intern.setSupervisor(supervisor);

        return internRepository.save(intern);
    }

    @Transactional
    public Intern update(UUID externalId, InternUpdateRequest updateRequest){
        Intern intern = internRepository.findByExternalId(externalId).orElseThrow(InternNotFoundException::new);

        User supervisor = intern.getSupervisor();
        if (!updateRequest.supervisorExternalId().equals(supervisor.getExternalId())) {
            supervisor = validateAndGetSupervisor(updateRequest.supervisorExternalId());
        }

        if (!updateRequest.enrollmentNumber().equals(intern.getEnrollmentNumber())) {
            if (internRepository.existsByEnrollmentNumber(updateRequest.enrollmentNumber())) {
                throw new InternEnrollmentNumberAlreadyExistsException(updateRequest.enrollmentNumber());
            }
        }

        UserUpdateRequest userData = updateRequest.user();
        userService.update(intern.getUser().getExternalId(), userData);
        
        internMapper.updateEntityFromRequest(updateRequest, intern);
        intern.setSupervisor(supervisor);

        return internRepository.save(intern);
    }
}
