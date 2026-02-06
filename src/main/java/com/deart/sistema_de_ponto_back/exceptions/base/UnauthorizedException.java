package com.deart.sistema_de_ponto_back.exceptions.base;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends HttpException{

    protected UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
    
}
