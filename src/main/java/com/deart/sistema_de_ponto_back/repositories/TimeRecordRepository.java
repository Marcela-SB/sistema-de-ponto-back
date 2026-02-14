package com.deart.sistema_de_ponto_back.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.deart.sistema_de_ponto_back.models.TimeRecord;
import com.deart.sistema_de_ponto_back.repositories.base.AuditableRepository;

public interface TimeRecordRepository extends AuditableRepository<TimeRecord>{
    List<TimeRecord> findAllByInternExternalId(UUID externalId);

    List<TimeRecord> findAllByRecordDate(LocalDate recordDate);

    List<TimeRecord> findAllByRecordDateBetween(LocalDate startDate, LocalDate endDate);

    List<TimeRecord> findAllByInternExternalIdAndRecordDateBetween(UUID externalId, LocalDate startDate, LocalDate endDate);

    List<TimeRecord> findAllByClockOutIsNull();

    List<TimeRecord> findAllByInternExternalIdAndClockOutIsNull(UUID externalId);
}
