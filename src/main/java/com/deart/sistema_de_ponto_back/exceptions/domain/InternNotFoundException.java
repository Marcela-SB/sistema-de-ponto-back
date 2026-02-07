package com.deart.sistema_de_ponto_back.exceptions.domain;

import com.deart.sistema_de_ponto_back.exceptions.base.EntityNotFoundException;

public class InternNotFoundException extends EntityNotFoundException{

    public InternNotFoundException() {
        super("Bolsista não encontrado.");
    }
    
}
