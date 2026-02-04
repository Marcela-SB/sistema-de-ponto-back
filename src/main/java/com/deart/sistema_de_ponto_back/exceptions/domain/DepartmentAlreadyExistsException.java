package com.deart.sistema_de_ponto_back.exceptions.domain;

import com.deart.sistema_de_ponto_back.exceptions.base.ConflictException;

public class DepartmentAlreadyExistsException extends ConflictException {
    public DepartmentAlreadyExistsException() {
        super("Departamento já existe!");
    }

    public DepartmentAlreadyExistsException(String identifier) {
        super("Departamento '"+ identifier +"' já existe!");
    }
}
