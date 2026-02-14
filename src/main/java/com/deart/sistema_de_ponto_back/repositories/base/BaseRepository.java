package com.deart.sistema_de_ponto_back.repositories.base;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {
    Optional<T> findByExternalId(UUID externalId);
    boolean existsByExternalId(UUID externalId);
}