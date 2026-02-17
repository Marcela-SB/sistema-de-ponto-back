package com.deart.sistema_de_ponto_back.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.deart.sistema_de_ponto_back.dtos.requests.ObservationInput;
import com.deart.sistema_de_ponto_back.dtos.requests.ObservationRequest;
import com.deart.sistema_de_ponto_back.enums.ObservationType;
import com.deart.sistema_de_ponto_back.exceptions.domain.ObservationNotFoundException;
import com.deart.sistema_de_ponto_back.mappers.ObservationMapper;
import com.deart.sistema_de_ponto_back.models.TimeRecord;
import com.deart.sistema_de_ponto_back.models.Observation;
import com.deart.sistema_de_ponto_back.repositories.ObservationRepository;

@Service
public class ObservationService {
    private final ObservationRepository repository;
    private final TimeRecordService timeRecordService;
    private final ObservationMapper mapper;

    public ObservationService(ObservationRepository repository, TimeRecordService timeRecordService, ObservationMapper mapper){
        this.repository = repository;
        this.timeRecordService = timeRecordService;
        this.mapper = mapper;
    }

    public List<Observation> findAll(){
        return repository.findAll();
    }

    public List<Observation> findAllByTimeRecord(TimeRecord timeRecord){
        return repository.findAllByTimeRecord(timeRecord);
    }

    public List<Observation> findAllByTimeRecordList(List<TimeRecord> timeRecords){
        return repository.findAllByTimeRecordIn(timeRecords);
    }

    public Observation findByExternalId(UUID externalId){
        return repository.findByExternalId(externalId)
            .orElseThrow(ObservationNotFoundException::new);
    }

    private Observation persistObservation(TimeRecord record, ObservationType type, String text) {
        Observation obs = repository.findByTimeRecordAndType(record, type)
                .map(existingObs -> {
                    mapper.updateEntityFromText(existingObs, text);
                    return existingObs;
                })
                .orElseGet(() -> 
                    mapper.toEntity(record, type, text)
                );

        return repository.save(obs);
    }

    public Observation processObservationFromRecord(TimeRecord record, ObservationInput input) {
        return persistObservation(record, input.type(), input.text());
    }
    
    public Observation processStandaloneObservation(ObservationRequest request) {
        TimeRecord record = timeRecordService.findByExternalId(request.timeRecordExternalId());
        return persistObservation(record, request.type(), request.text());
    }

    public void delete(UUID externalId){
        Observation obs = findByExternalId(externalId);
        repository.delete(obs);
    }

}
