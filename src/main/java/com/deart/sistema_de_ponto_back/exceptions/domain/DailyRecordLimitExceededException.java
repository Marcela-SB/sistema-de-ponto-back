package com.deart.sistema_de_ponto_back.exceptions.domain;

import java.time.LocalDate;

import com.deart.sistema_de_ponto_back.exceptions.base.UnprocessableEntityException;

public class DailyRecordLimitExceededException extends UnprocessableEntityException{

    public DailyRecordLimitExceededException() {
        super("Limite de registros diário excedido.");
    }
    
    public DailyRecordLimitExceededException(LocalDate date) {
        super("Limite de registros diário para a data " + date + " foi  excedido.");
    }
    
}
