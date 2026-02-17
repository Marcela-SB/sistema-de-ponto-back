package com.deart.sistema_de_ponto_back.repositories;

import com.deart.sistema_de_ponto_back.models.Observation;
import com.deart.sistema_de_ponto_back.repositories.base.BaseRepository;
import java.util.List;
import java.util.Optional;
import com.deart.sistema_de_ponto_back.enums.ObservationType;
import com.deart.sistema_de_ponto_back.models.TimeRecord;


public interface ObservationRepository extends BaseRepository<Observation, Long>{
    
    Optional<Observation> findByTimeRecordAndType(TimeRecord timeRecord, ObservationType type);

    List<Observation> findAllByTimeRecord(TimeRecord timeRecord);

    List<Observation> findAllByTimeRecordIn(List<TimeRecord> timeRecords);
    
}
