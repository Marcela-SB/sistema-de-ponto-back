package com.deart.sistema_de_ponto_back.dtos.responses;

import java.util.UUID;

public record InternResponse(
    UserResponse user,
    UUID externalId,
    String enrollmentNumber,
    String supervisorName,
    UUID supervisorExternalId
) {}
