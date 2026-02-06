package com.deart.sistema_de_ponto_back.exceptions.domain;

import com.deart.sistema_de_ponto_back.exceptions.base.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException() {
        super("Usuário não encontrado!");
    }
}
