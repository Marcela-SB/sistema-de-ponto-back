package com.deart.sistema_de_ponto_back.dtos.responses;

import java.util.UUID;

public record LoginResponse(
    UUID externalId,
    String name,
    String role,
    String token,
    Long expiresIn
) {}
