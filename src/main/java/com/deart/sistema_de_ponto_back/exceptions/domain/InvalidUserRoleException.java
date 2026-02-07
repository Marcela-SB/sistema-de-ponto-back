package com.deart.sistema_de_ponto_back.exceptions.domain;

import com.deart.sistema_de_ponto_back.exceptions.base.BadRequestException;

public class InvalidUserRoleException extends BadRequestException{

    public InvalidUserRoleException() {
        super("O usuário selecionado não possui o cargo necessário para a ação .");
    }

    public InvalidUserRoleException(String role) {
        super("O usuário selecionado não possui o cargo de " + role + ".");
    }
    
}
