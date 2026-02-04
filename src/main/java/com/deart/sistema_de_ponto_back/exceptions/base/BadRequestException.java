package com.deart.sistema_de_ponto_back.exceptions.base;

import org.springframework.http.HttpStatus;

public abstract class BadRequestException extends HttpException {
    protected BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
