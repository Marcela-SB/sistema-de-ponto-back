package com.deart.sistema_de_ponto_back.exceptions;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.deart.sistema_de_ponto_back.exceptions.base.HttpException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<Map<String, Object>> handleHttpException(HttpException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(ex.getResponseBody());
    }

    // Captura erros inesperados (NullPointerException, erro de banco, etc)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralErrors(Exception ex) {
        Map<String, Object> error = new LinkedHashMap<>();

        error.put("timestamp", java.time.OffsetDateTime.now().toString());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        error.put("message", "Ocorreu um erro interno no servidor.");
        
        // ex.printStackTrace(); 

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}