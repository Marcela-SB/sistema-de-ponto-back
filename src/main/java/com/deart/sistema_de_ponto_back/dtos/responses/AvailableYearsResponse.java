package com.deart.sistema_de_ponto_back.dtos.responses;

import java.util.List;

public record AvailableYearsResponse(
    List<Integer> years
) {}
