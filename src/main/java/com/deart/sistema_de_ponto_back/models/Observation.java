package com.deart.sistema_de_ponto_back.models;

import com.deart.sistema_de_ponto_back.enums.ObservationType;
import com.deart.sistema_de_ponto_back.models.abstracts.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "observations",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "obs_uk_time_record_type",
            columnNames = {"time_record_id", "type"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Observation extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_record_id", nullable = false)
    private TimeRecord timeRecord;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ObservationType type;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;
}