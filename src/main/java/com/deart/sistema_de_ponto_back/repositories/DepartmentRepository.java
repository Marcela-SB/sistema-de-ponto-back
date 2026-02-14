package com.deart.sistema_de_ponto_back.repositories;

import java.util.Optional;
import java.util.UUID;

import com.deart.sistema_de_ponto_back.models.Department;
import com.deart.sistema_de_ponto_back.repositories.base.BaseRepository;

public interface DepartmentRepository extends BaseRepository<Department, Long>{
    
    Optional<Department> findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndExternalIdNot(String name, UUID externalId);
}
