package com.deart.sistema_de_ponto_back.dtos.requests;

import java.util.UUID;

import com.deart.sistema_de_ponto_back.enums.ObservationType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ObservationRequest(
    @NotNull
    UUID timeRecordExternalId,

    @NotNull
    ObservationType type,

    @NotBlank
    String text
) {}
