package com.deart.sistema_de_ponto_back.exceptions.base;

import org.springframework.http.HttpStatus;

public abstract class EntityNotFoundException extends HttpException {
    protected EntityNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
