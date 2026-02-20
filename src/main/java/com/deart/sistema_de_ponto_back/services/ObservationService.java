package com.deart.sistema_de_ponto_back.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.deart.sistema_de_ponto_back.dtos.requests.ObservationInput;
import com.deart.sistema_de_ponto_back.dtos.requests.ObservationRequest;
import com.deart.sistema_de_ponto_back.enums.ObservationType;
import com.deart.sistema_de_ponto_back.exceptions.domain.ObservationNotFoundException;
import com.deart.sistema_de_ponto_back.exceptions.domain.TimeRecordNotFoundException;
import com.deart.sistema_de_ponto_back.mappers.ObservationMapper;
import com.deart.sistema_de_ponto_back.models.TimeRecord;
import com.deart.sistema_de_ponto_back.models.Observation;
import com.deart.sistema_de_ponto_back.repositories.ObservationRepository;
import com.deart.sistema_de_ponto_back.repositories.TimeRecordRepository;

import jakarta.transaction.Transactional;

@Service
public class ObservationService {
    private final ObservationRepository observationRepository;
    private final TimeRecordRepository recordRepository;
    private final ObservationMapper mapper;

    public ObservationService(ObservationRepository observationRepository, TimeRecordRepository recordRepository, ObservationMapper mapper){
        this.observationRepository = observationRepository;
        this.recordRepository = recordRepository;
        this.mapper = mapper;
    }

    public List<Observation> findAll(){
        return observationRepository.findAll();
    }

    public List<Observation> findAllByTimeRecord(TimeRecord timeRecord){
        return observationRepository.findAllByTimeRecord(timeRecord);
    }

    public List<Observation> findAllByTimeRecordList(List<TimeRecord> timeRecords){
        return observationRepository.findAllByTimeRecordIn(timeRecords);
    }

    public Observation findByExternalId(UUID externalId){
        return observationRepository.findByExternalId(externalId)
            .orElseThrow(ObservationNotFoundException::new);
    }

    private Observation persistObservation(TimeRecord record, ObservationType type, String text) {
        Observation obs = record.getObservations().stream()
                .filter(o -> o.getType().equals(type))
                .findFirst()
                .orElseGet(() -> {
                    Observation newObs = mapper.toEntity(record, type, text);
                    record.addObservation(newObs);
                    return newObs;
                });

        if (!text.equals(obs.getText())) {
            obs.setText(text);
        }
        return obs;
    }

    @Transactional
    public Observation processObservationFromRecord(TimeRecord record, ObservationType type, ObservationInput obs) {
        return persistObservation(record, type, obs.text());
    }
    
    @Transactional
    public Observation processStandaloneObservation(ObservationRequest request) {
        TimeRecord record = recordRepository.findByExternalId(request.timeRecordExternalId())
                .orElseThrow(TimeRecordNotFoundException::new);
        return persistObservation(record, request.type(), request.text());
    }

    @Transactional
    public void delete(UUID externalId){
        Observation obs = findByExternalId(externalId);
        TimeRecord record = obs.getTimeRecord();
        record.getObservations().remove(obs);
    }

}
