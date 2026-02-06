package com.deart.sistema_de_ponto_back.exceptions.domain;

import com.deart.sistema_de_ponto_back.exceptions.base.ConflictException;

public class UserCpfAlreadyExistsException extends ConflictException {
    
    public UserCpfAlreadyExistsException() {
        super("CPF já cadastrado em outro usuário.");
    }

    public UserCpfAlreadyExistsException(String cpf) {
        super("CPF '" + cpf + "' já cadastrado em outro usuário.");
    }
}
