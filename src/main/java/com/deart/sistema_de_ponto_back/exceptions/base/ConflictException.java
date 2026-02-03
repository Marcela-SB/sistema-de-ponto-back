package com.deart.sistema_de_ponto_back.exceptions.base;

import org.springframework.http.HttpStatus;

public abstract class ConflictException extends HttpException {
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}