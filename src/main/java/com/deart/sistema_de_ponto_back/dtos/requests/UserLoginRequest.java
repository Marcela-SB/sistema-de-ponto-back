package com.deart.sistema_de_ponto_back.dtos.requests;

public record UserLoginRequest(
    String cpf,
    String password
    // adicionar alguma verificação de que está presente no local
) {}
