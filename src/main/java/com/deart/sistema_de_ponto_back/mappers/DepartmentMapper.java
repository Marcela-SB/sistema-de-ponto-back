package com.deart.sistema_de_ponto_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.deart.sistema_de_ponto_back.dtos.requests.DepartmentRequest;
import com.deart.sistema_de_ponto_back.dtos.responses.DepartmentResponse;
import com.deart.sistema_de_ponto_back.models.Department;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    Department toEntity(DepartmentRequest request);

    DepartmentResponse toResponse(Department department);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "users", ignore = true)
    void updateEntityFromRequest(DepartmentRequest request, @MappingTarget Department entity);
}
