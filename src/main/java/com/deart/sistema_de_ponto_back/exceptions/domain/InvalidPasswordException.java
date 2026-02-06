package com.deart.sistema_de_ponto_back.exceptions.domain;

import com.deart.sistema_de_ponto_back.exceptions.base.UnauthorizedException;

public class InvalidPasswordException extends UnauthorizedException {

    public InvalidPasswordException() {
        super("Senha atual incorreta.");
    }
    
}
