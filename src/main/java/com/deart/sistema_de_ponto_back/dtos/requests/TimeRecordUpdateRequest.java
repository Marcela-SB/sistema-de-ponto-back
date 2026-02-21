package com.deart.sistema_de_ponto_back.dtos.requests;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record TimeRecordUpdateRequest(
    @NotNull
    LocalTime clockIn,
    
    LocalTime clockOut,
    ObservationInput supervisorObservation
) {}