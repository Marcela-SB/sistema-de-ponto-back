package com.deart.sistema_de_ponto_back.dtos.requests;

import java.util.UUID;

import com.deart.sistema_de_ponto_back.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(
    @NotBlank
    String name,

    @NotBlank
    String cpf,

    @NotBlank
    @Email
    String email,

    @NotNull
    UserRole role,
    
    UUID departmentExternalId
) {}
