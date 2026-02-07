package com.deart.sistema_de_ponto_back.exceptions.domain;

import com.deart.sistema_de_ponto_back.exceptions.base.BadRequestException;

public class InactiveUserException extends BadRequestException {
    
    public InactiveUserException() {
        super("Operação não permitida: O usuário informado está inativo no sistema.");
    }

    public InactiveUserException(String role) {
        super("Operação não permitida: O " + role + " informado está inativo no sistema.");
    }
}