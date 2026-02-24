package com.deart.sistema_de_ponto_back.mappers;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.deart.sistema_de_ponto_back.dtos.requests.UserCreateRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.UserUpdateRequest;
import com.deart.sistema_de_ponto_back.dtos.responses.LoginResponse;
import com.deart.sistema_de_ponto_back.dtos.responses.UserResponse;
import com.deart.sistema_de_ponto_back.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "active", ignore = true)
    User toEntity(UserCreateRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "department", ignore = true)
    User updateEntityFromRequest(UserUpdateRequest request, @MappingTarget User user);

    @Mapping(target = "role", source = "role.description")
    @Mapping(target = "department", source = "department.name")
    UserResponse toResponse(User user);

    @Mapping(target = "role", source = "user.role.description")
    LoginResponse toLoginResponse(User user, UUID internExternalId, String token, Long expiresIn);
}
