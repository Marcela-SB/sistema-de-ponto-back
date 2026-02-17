package com.deart.sistema_de_ponto_back.exceptions.domain;

import java.time.LocalDate;

import com.deart.sistema_de_ponto_back.exceptions.base.EntityNotFoundException;

public class TimeRecordNotFoundException extends EntityNotFoundException{

    public TimeRecordNotFoundException() {
        super("Registro de frequência não encontrado.");
    }

    public TimeRecordNotFoundException(LocalDate date) {
        super("Registro da data " + date + " não encontrado.");
    }
    
}
