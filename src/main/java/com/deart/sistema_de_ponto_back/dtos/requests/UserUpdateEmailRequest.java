package com.deart.sistema_de_ponto_back.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateEmailRequest(
    @NotBlank @Email String email
) {}
