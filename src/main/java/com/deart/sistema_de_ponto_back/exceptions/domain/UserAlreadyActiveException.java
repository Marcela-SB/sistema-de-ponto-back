package com.deart.sistema_de_ponto_back.exceptions.domain;

import com.deart.sistema_de_ponto_back.exceptions.base.ConflictException;

public class UserAlreadyActiveException extends ConflictException {
    
    public UserAlreadyActiveException() {
        super("Este usuário já está ativo.");
    }
}
