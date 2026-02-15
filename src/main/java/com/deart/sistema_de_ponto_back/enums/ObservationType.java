package com.deart.sistema_de_ponto_back.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ObservationType {
    SUPERVISOR("Supervisor"),
    INTERN("Bolsista");

    private final String description;
}
