package com.deart.sistema_de_ponto_back.dtos.responses;

import java.util.UUID;

public record DepartmentResponse(
    UUID externalId,
    String name
) {}
