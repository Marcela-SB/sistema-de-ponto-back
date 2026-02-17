package com.deart.sistema_de_ponto_back.exceptions.domain;

import com.deart.sistema_de_ponto_back.exceptions.base.EntityNotFoundException;

public class ObservationNotFoundException extends EntityNotFoundException{

    public ObservationNotFoundException() {
        super("Observação não encontrada.");
    }
    
}
