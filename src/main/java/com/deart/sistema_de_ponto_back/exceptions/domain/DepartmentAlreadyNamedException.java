package com.deart.sistema_de_ponto_back.exceptions.domain;

import com.deart.sistema_de_ponto_back.exceptions.base.ConflictException;

public class DepartmentAlreadyNamedException extends ConflictException {

    public DepartmentAlreadyNamedException(String name) {
        super("Não é possível renomear departamento pois já possui o nome '"+ name + "'.");
    }
    
}
