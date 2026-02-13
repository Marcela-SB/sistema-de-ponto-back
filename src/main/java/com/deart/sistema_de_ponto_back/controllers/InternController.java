package com.deart.sistema_de_ponto_back.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.deart.sistema_de_ponto_back.dtos.requests.InternCreateRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.InternUpdateRequest;
import com.deart.sistema_de_ponto_back.dtos.responses.InternResponse;
import com.deart.sistema_de_ponto_back.mappers.InternMapper;
import com.deart.sistema_de_ponto_back.models.Intern;
import com.deart.sistema_de_ponto_back.services.InternService;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/interns")
public class InternController {
    private final InternService service;
    private final InternMapper mapper;

    public InternController(InternService service, InternMapper mapper){
        this.service = service;
        this.mapper = mapper;
    }


    @GetMapping
    public ResponseEntity<List<InternResponse>> findAllInterns() {
        List<InternResponse> interns = service.findAll()
            .stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok().body(interns);
    }

    @GetMapping("/actives")
    public ResponseEntity<List<InternResponse>> findAllActiveInterns() {
        List<InternResponse> interns = service.findAllActives()
            .stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok().body(interns);
    }

    @GetMapping("/supervisor/{externalId}")
    public ResponseEntity<List<InternResponse>> findAllInternsBySupervisor(@PathVariable UUID externalId) {
        List<InternResponse> interns = service.findAllBySupervisor(externalId)
            .stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok().body(interns);
    }

    // verificar ROLE (apenas ADMIN ou SUPERV. responsável)
    @PostMapping
    public ResponseEntity<InternResponse> createIntern(@RequestBody InternCreateRequest createRequest) {
        Intern entity = service.create(createRequest);
        InternResponse response = mapper.toResponse(entity);    
        return ResponseEntity.ok().body(response);
    }
    
    // verificar ROLE (apenas ADMIN ou SUPERV. responsável)
    @PutMapping("/update/{externalId}")
    public ResponseEntity<InternResponse> updateIntern(@PathVariable UUID externalId, @RequestBody InternUpdateRequest updateRequest) {
        Intern entity = service.update(externalId, updateRequest);
        InternResponse response = mapper.toResponse(entity);
        return ResponseEntity.ok().body(response);
    }
    
    
}
