package com.deart.sistema_de_ponto_back.exceptions.domain;

import java.time.LocalDate;

import com.deart.sistema_de_ponto_back.exceptions.base.UnprocessableEntityException;

public class DailyWorkdayAlreadyClosedException  extends UnprocessableEntityException{

    public DailyWorkdayAlreadyClosedException() {
        super("O registro de frequência para esta data já foi encerrado e não permite novos registros.");
    }

    public DailyWorkdayAlreadyClosedException(LocalDate date) {
        super("O registro de frequência para a data " + date + " já foi encerrado.");
    }
}
