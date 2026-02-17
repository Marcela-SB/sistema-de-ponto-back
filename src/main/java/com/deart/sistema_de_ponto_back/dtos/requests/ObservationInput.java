package com.deart.sistema_de_ponto_back.dtos.requests;

import com.deart.sistema_de_ponto_back.enums.ObservationType;

public record ObservationInput(
    ObservationType type,
    String text
) {}
