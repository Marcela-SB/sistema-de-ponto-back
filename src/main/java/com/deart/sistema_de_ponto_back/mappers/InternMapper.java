package com.deart.sistema_de_ponto_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.deart.sistema_de_ponto_back.dtos.requests.InternCreateRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.InternUpdateRequest;
import com.deart.sistema_de_ponto_back.dtos.responses.InternResponse;
import com.deart.sistema_de_ponto_back.models.Intern;
import com.deart.sistema_de_ponto_back.models.User;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface InternMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supervisor", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "enrollmentNumber", source = "request.enrollmentNumber")
    Intern toEntityFromCreate(InternCreateRequest request, User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "supervisor", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    void updateEntityFromRequest(InternUpdateRequest request, @MappingTarget Intern intern);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "supervisorName", source = "supervisor.name")
    @Mapping(target = "supervisorExternalId", source = "supervisor.externalId")
    InternResponse toResponse(Intern intern);
}
