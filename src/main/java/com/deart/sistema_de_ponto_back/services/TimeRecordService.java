package com.deart.sistema_de_ponto_back.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.deart.sistema_de_ponto_back.exceptions.domain.TimeRecordNotFoundException;
import com.deart.sistema_de_ponto_back.models.TimeRecord;
import com.deart.sistema_de_ponto_back.repositories.TimeRecordRepository;

@Service
public class TimeRecordService {
    private final TimeRecordRepository recordRepository;

    public TimeRecordService(TimeRecordRepository recordRepository){
        this.recordRepository = recordRepository;
    }

    public TimeRecord findByExternalId(UUID externalId){
        return recordRepository.findByExternalId(externalId)
                .orElseThrow(TimeRecordNotFoundException::new);
    }

}
