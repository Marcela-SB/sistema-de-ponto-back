package com.deart.sistema_de_ponto_back.dtos.responses;

import java.util.UUID;

public record UserResponse(
    UUID externalId,
    String name,
    String cpf,
    String email,
    String role,
    String departament
) {}
