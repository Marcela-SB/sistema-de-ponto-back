package com.deart.sistema_de_ponto_back.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deart.sistema_de_ponto_back.dtos.requests.DepartmentRequest;
import com.deart.sistema_de_ponto_back.dtos.responses.DepartmentResponse;
import com.deart.sistema_de_ponto_back.mappers.DepartmentMapper;
import com.deart.sistema_de_ponto_back.models.Department;
import com.deart.sistema_de_ponto_back.services.DepartmentService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/departments")
public class DepartmentController {
    private final DepartmentService service;
    private final DepartmentMapper mapper;

    public DepartmentController(DepartmentService service, DepartmentMapper mapper){
        this.service = service;
        this.mapper = mapper;
    }


    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        List<DepartmentResponse> departments = service.findAll()
            .stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok().body(departments);
    }

    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(@RequestBody @Valid DepartmentRequest request) {
        Department entity = service.create(request);
        DepartmentResponse response = mapper.toResponse(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{externalId}")
    public ResponseEntity<DepartmentResponse> updateDepartment(@PathVariable UUID externalId, @RequestBody @Valid DepartmentRequest request) {
        Department entity = service.update(externalId, request);
        DepartmentResponse response = mapper.toResponse(entity);
        return ResponseEntity.ok().body(response);
    }
    
    @DeleteMapping("/{externalId}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable UUID externalId) {
        service.delete(externalId);
        return ResponseEntity.noContent().build();
    }
    
}
