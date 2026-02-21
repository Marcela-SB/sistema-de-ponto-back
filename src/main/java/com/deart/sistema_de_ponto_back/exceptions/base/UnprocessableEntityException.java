package com.deart.sistema_de_ponto_back.exceptions.base;

import org.springframework.http.HttpStatus;

public class UnprocessableEntityException extends HttpException{

    public UnprocessableEntityException() {
        super("A regra de negócio foi violada.", HttpStatus.UNPROCESSABLE_CONTENT);
    }

    public UnprocessableEntityException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_CONTENT);
    }
    
}
