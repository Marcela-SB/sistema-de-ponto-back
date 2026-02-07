package com.deart.sistema_de_ponto_back.dtos.requests;

import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InternUpdateRequest(
    @NotNull
    @Valid
    UserUpdateRequest user,

    @NotBlank
    String enrollmentNumber,
    
    @NotNull
    UUID supervisorExternalId
) {}
