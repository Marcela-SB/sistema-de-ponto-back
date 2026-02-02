package com.deart.sistema_de_ponto_back.Model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "time_record_observations")
@Getter
@Setter
@NoArgsConstructor
public class TimeRecordObservation {
    @Id
    private Long id;

    @Column(unique = true, nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID externalId = UUID.randomUUID();

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "time_record_id")
    private TimeRecord timeRecord;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;
}