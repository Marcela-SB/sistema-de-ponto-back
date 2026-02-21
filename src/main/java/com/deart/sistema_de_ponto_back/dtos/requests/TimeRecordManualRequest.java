package com.deart.sistema_de_ponto_back.dtos.requests;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record TimeRecordManualRequest(
    @NotNull
    UUID internExternalId,

    @NotNull
    LocalDate recordDate,

    @NotNull
    LocalTime clockIn,
    
    LocalTime clockOut,
    ObservationInput supervisorObservation
) {}
