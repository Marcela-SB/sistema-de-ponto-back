package com.deart.sistema_de_ponto_back.exceptions.domain;

import com.deart.sistema_de_ponto_back.exceptions.base.UnauthorizedException;

public class InvalidCredentialsException extends UnauthorizedException {

    public InvalidCredentialsException() {
        super("CPF ou senha inválidos.");
    }
    
}
