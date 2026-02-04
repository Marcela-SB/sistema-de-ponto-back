package com.deart.sistema_de_ponto_back.exceptions.domain;

import com.deart.sistema_de_ponto_back.exceptions.base.EntityNotFoundException;

public class DepartmentNotFoundException extends EntityNotFoundException{

    public DepartmentNotFoundException() {
        super("Departamento não encontrado!");
    }

    public DepartmentNotFoundException(String identifier) {
        super("Departamento '"+ identifier +"' não encontrado!");
    }
    
}
