package com.deart.sistema_de_ponto_back.dtos.responses;

import java.util.UUID;

public record TimeRecordResponse(
    UUID externalId,
    UUID internExternalId,
    String recordDate,
    String clockIn,
    String clockOut,
    String totalHours,
    ObservationResponse internObservation,
    ObservationResponse supervisorObservation
) {}
