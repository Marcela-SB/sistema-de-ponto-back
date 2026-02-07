package com.deart.sistema_de_ponto_back.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.deart.sistema_de_ponto_back.dtos.requests.DepartmentRequest;
import com.deart.sistema_de_ponto_back.exceptions.domain.DepartmentAlreadyExistsException;
import com.deart.sistema_de_ponto_back.exceptions.domain.DepartmentAlreadyNamedException;
import com.deart.sistema_de_ponto_back.exceptions.domain.DepartmentNotFoundException;
import com.deart.sistema_de_ponto_back.mappers.DepartmentMapper;
import com.deart.sistema_de_ponto_back.models.Department;
import com.deart.sistema_de_ponto_back.repositories.DepartmentRepository;

import jakarta.transaction.Transactional;


@Service
public class DepartmentService {
    private final DepartmentMapper mapper;
    private final DepartmentRepository repository;

    public DepartmentService(DepartmentMapper mapper, DepartmentRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }


    public List<Department> findAll(){
        return repository.findAll();
    }

    public Department findByExternalId(UUID externalId){
        return repository.findByExternalId(externalId)
            .orElseThrow(DepartmentNotFoundException::new);
    }

    @Transactional
    public Department create(DepartmentRequest request) {
        if (repository.existsByName(request.name())) {
            throw new DepartmentAlreadyExistsException(request.name());
        }
        Department entity = mapper.toEntity(request);
        return repository.save(entity);
    }

    @Transactional
    public Department update(UUID externalId, DepartmentRequest request) {
        Department department = findByExternalId(externalId);

        if (department.getName().equals(request.name())) {
            throw new DepartmentAlreadyNamedException(request.name());
        }

        if (repository.existsByNameAndExternalIdNot(request.name(), externalId)) {
            throw new DepartmentAlreadyExistsException(request.name());
        }

        mapper.updateEntityFromRequest(request, department);
        return repository.save(department);
    }

    @Transactional
    public void delete(UUID externalId) {
        Department department = findByExternalId(externalId);
        repository.delete(department);
    }
}
