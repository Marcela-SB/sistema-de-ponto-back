package com.deart.sistema_de_ponto_back.Model.Abstract;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class BaseEntity {

    @Column(name = "external_id", columnDefinition = "BINARY(16)", unique = true, nullable = false, updatable = false)
    private UUID externalId;

    
    @PrePersist
    protected void onCreate() {
        if (this.externalId == null) {
            this.externalId = UUID.randomUUID();
        }
    }
}
