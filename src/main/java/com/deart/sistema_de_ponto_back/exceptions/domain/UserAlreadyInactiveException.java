package com.deart.sistema_de_ponto_back.exceptions.domain;

import com.deart.sistema_de_ponto_back.exceptions.base.ConflictException;

public class UserAlreadyInactiveException extends ConflictException {
    
    public UserAlreadyInactiveException() {
        super("Este usuário já está desativado.");
    }
}
