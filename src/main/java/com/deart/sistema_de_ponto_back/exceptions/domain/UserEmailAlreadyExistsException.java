package com.deart.sistema_de_ponto_back.exceptions.domain;

import com.deart.sistema_de_ponto_back.exceptions.base.ConflictException;

public class UserEmailAlreadyExistsException extends ConflictException {
    
    public UserEmailAlreadyExistsException() {
        super("Email já cadastrado em outro usuário.");
    }

    public UserEmailAlreadyExistsException(String email) {
        super("Email '" + email + "' já cadastrado em outro usuário.");
    }
    
}
