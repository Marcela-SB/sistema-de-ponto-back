package com.deart.sistema_de_ponto_back.exceptions.base;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * Classe base para todas as exceções de negócio que possuem um status HTTP associado.
 */
@Getter
public abstract class HttpException extends RuntimeException {
    
    private final HttpStatus status;

    public HttpException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    /**
     * Monta a estrutura do erro para ser retornada no corpo da resposta (JSON).
     */
    public Map<String, Object> getResponseBody() {
        Map<String, Object> body = new LinkedHashMap<>();
        
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", this.getMessage());
        
        return body;
    }
}