package com.deart.sistema_de_ponto_back.exceptions.domain;

import com.deart.sistema_de_ponto_back.exceptions.base.ConflictException;

public class InternEnrollmentNumberAlreadyExistsException extends ConflictException{

    public InternEnrollmentNumberAlreadyExistsException() {
        super("Número de matrícula já cadastrado em outro usuário.");
    }

    public InternEnrollmentNumberAlreadyExistsException(String enrollmentNumber) {
        super("Número de matrícula '" + enrollmentNumber+ "' já cadastrado em outro usuário.");
    }
    
}
