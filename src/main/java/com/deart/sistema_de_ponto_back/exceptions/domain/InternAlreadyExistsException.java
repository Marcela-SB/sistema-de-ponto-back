package com.deart.sistema_de_ponto_back.exceptions.domain;

import com.deart.sistema_de_ponto_back.exceptions.base.ConflictException;

public class InternAlreadyExistsException extends ConflictException {

    public InternAlreadyExistsException() {
        super("Este usuário já está cadastrado como estagiário.");
    }
    
}
