package com.deart.sistema_de_ponto_back.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record UserUpdatePasswordRequest(
    @NotBlank
    String currentPassword, 

    @NotBlank
    String newPassword
) {}