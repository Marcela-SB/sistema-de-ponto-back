package com.deart.sistema_de_ponto_back.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.deart.sistema_de_ponto_back.dtos.requests.ObservationInput;
import com.deart.sistema_de_ponto_back.dtos.requests.TimeRecordManualRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.TimeRecordUpdateRequest;
import com.deart.sistema_de_ponto_back.dtos.responses.AvailableYearsResponse;
import com.deart.sistema_de_ponto_back.dtos.responses.TimeRecordResponse;
import com.deart.sistema_de_ponto_back.mappers.TimeRecordMapper;
import com.deart.sistema_de_ponto_back.models.TimeRecord;
import com.deart.sistema_de_ponto_back.services.TimeRecordService;

import jakarta.validation.Valid;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/time-records")
public class TimeRecordController {
    private final TimeRecordService service;
    private final TimeRecordMapper mapper;

    public TimeRecordController(TimeRecordService service, TimeRecordMapper mapper){
        this.service = service;
        this.mapper = mapper;
    }


    @GetMapping("/allToday")
    public ResponseEntity<List<TimeRecordResponse>> getAllToday() {
        List<TimeRecordResponse> responses = service.findAllToday()
                .stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/intern/{externalId}")
    public ResponseEntity<List<TimeRecordResponse>> findAllByIntern(@PathVariable UUID externalId) {
        List<TimeRecordResponse> responses = service.findAllByIntern(externalId)
            .stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/intern/{externalId}/today")
    public ResponseEntity<TimeRecordResponse> getToday(@PathVariable UUID externalId) {
        return service.findByInternToday(externalId)
                .map(record -> ResponseEntity.ok(mapper.toResponse(record)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
    
    @GetMapping("/intern/{externalId}/year")
    public ResponseEntity<List<TimeRecordResponse>> getAllByInternInThisYear(@PathVariable UUID externalId) {
        List<TimeRecordResponse> responses = service.findAllByInternInThisYear(externalId)
            .stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/intern/{externalId}/period")
    public ResponseEntity<List<TimeRecordResponse>> getAllByInternInPeriod(
        @PathVariable UUID externalId, 
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth startMonth, 
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth endMonth
    ) {
        List<TimeRecordResponse> responses = service.findAllByInternInPeriod(externalId, startMonth, endMonth)
                .stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/intern/{externalId}")
    public ResponseEntity<TimeRecordResponse> internPersonalRegister(@PathVariable UUID externalId, @Valid @RequestBody(required = false) ObservationInput obs) {
        TimeRecord record = service.register(externalId, obs);
        return ResponseEntity.ok(mapper.toResponse(record));
    }

    @PostMapping("/manual")
    public ResponseEntity<TimeRecordResponse> manualRegister(@Valid @RequestBody TimeRecordManualRequest request) {
        TimeRecord record = service.manualRegister(request);
        TimeRecordResponse response = mapper.toResponse(record);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{externalId}")
    public ResponseEntity<TimeRecordResponse> updateRecord(@PathVariable UUID externalId, @Valid @RequestBody TimeRecordUpdateRequest updateRequest) {
        TimeRecord record = service.update(externalId, updateRequest);
        TimeRecordResponse response = mapper.toResponse(record);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{externalId}")
    public ResponseEntity<Void> deleteRecord(@PathVariable UUID externalId) {
        service.delete(externalId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-years")
    public ResponseEntity<AvailableYearsResponse> getYearsWithRecords() {
        List<Integer> years = service.getAvailableYears();
        return ResponseEntity.ok(new AvailableYearsResponse(years));
    }
    
}
