package com.deart.sistema_de_ponto_back.dtos.requests;

import jakarta.validation.constraints.NotEmpty;

public record DepartmentRequest(
    @NotEmpty(message = "O nome do setor é obrigatório")
    String name
) {}
