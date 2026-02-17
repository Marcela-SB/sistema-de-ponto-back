package com.deart.sistema_de_ponto_back.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deart.sistema_de_ponto_back.dtos.requests.ObservationRequest;
import com.deart.sistema_de_ponto_back.dtos.responses.ObservationResponse;
import com.deart.sistema_de_ponto_back.mappers.ObservationMapper;
import com.deart.sistema_de_ponto_back.models.Observation;
import com.deart.sistema_de_ponto_back.services.ObservationService;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/observations")
public class ObservationController {
    private final ObservationService service;
    private final ObservationMapper mapper;

    public ObservationController(ObservationService service, ObservationMapper mapper){
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ObservationResponse> upsert(@RequestBody ObservationRequest request) {
        Observation obs = service.processStandaloneObservation(request);
        ObservationResponse response = mapper.toResponse(obs);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{externalId}")
    public ResponseEntity<Void> deleteObservation(@PathVariable UUID externalId){
        service.delete(externalId);
        return ResponseEntity.noContent().build();
    }
    
}
