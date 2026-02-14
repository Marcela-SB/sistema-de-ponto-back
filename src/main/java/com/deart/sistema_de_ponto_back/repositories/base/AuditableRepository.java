package com.deart.sistema_de_ponto_back.repositories.base;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.NoRepositoryBean;

import com.deart.sistema_de_ponto_back.models.abstracts.AuditableEntity;

@NoRepositoryBean
public interface AuditableRepository<T extends AuditableEntity> extends BaseRepository<T, Long> {
    List<T> findByCreatedBy_ExternalId(UUID externalId);

    List<T> findByUpdatedBy_ExternalId(UUID externalId);

    List<T> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<T> findByUpdatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<T> findByCreatedAtBetweenOrUpdatedAtBetween(
        LocalDateTime startCreate, LocalDateTime endCreate,
        LocalDateTime startUpdate, LocalDateTime endUpdate
    );
}
