package com.deart.sistema_de_ponto_back.dtos.responses;

import java.util.UUID;

import com.deart.sistema_de_ponto_back.enums.ObservationType;

public record ObservationResponse(
    UUID externalId,
    UUID timeRecordExternalId,
    ObservationType type,
    String text,
    String lastUpdate
) {}
