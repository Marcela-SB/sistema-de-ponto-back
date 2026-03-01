package com.deart.sistema_de_ponto_back.dtos.responses;

import java.util.UUID;

public record ObservationResponse(
    UUID externalId,
    UUID timeRecordExternalId,
    String type,
    String text,
    String lastUpdate
) {}
