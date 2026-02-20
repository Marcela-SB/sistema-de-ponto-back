package com.deart.sistema_de_ponto_back.models;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import com.deart.sistema_de_ponto_back.models.abstracts.AuditableEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "time_records", 
    uniqueConstraints = {
        @UniqueConstraint(
            name = "records_uk_intern_record_date", 
            columnNames = {"intern_id", "record_date"}
        )
    },
    indexes = {
        // Acelera buscas por um estagiário específico
        @Index(name = "records_idx_timerecord_intern", columnList = "intern_id"),
        
        // Acelera buscas por data (filtros de hoje, ontem, etc)
        @Index(name = "records_idx_timerecord_date", columnList = "record_date"),
        
        // O "Super Índice": Acelera a query de um estagiário dentro de um mês (ID + Data)
        @Index(name = "records_idx_intern_date_composite", columnList = "intern_id, record_date")
    }
)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TimeRecord extends AuditableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intern_id", nullable = false)
    private Intern intern;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "clock_in", nullable = false)
    private LocalTime clockIn;

    @Column(name = "clock_out")
    private LocalTime clockOut;

    // Configure your database:
    //     ALTER TABLE time_records 
    //     MODIFY COLUMN total_hours TIME 
    //     GENERATED ALWAYS AS (TIMEDIFF(clock_out, clock_in)) STORED;
    @Column(name = "total_hours", insertable = false, updatable = false)
    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    private LocalTime totalHours;

    @OneToMany(mappedBy = "timeRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Observation> observations = new ArrayList<>();


    // para situações onde necessita da informação antes do banco retornar o valor
    public void calculateTotalHours() {
        if (this.clockIn != null && this.clockOut != null) {
            Duration duration = Duration.between(this.clockIn, this.clockOut);
        
            long seconds = duration.getSeconds();
            
            this.totalHours = LocalTime.ofSecondOfDay(Math.max(0, seconds));
        } else {
            this.totalHours = null;
        }
    }

    public String getTotalHoursFormatted() {
        if (totalHours == null) return "--:--";
        return String.format("%02d:%02d", totalHours.getHour(), totalHours.getMinute());
    }
    
    public void addObservation(Observation observation) {
        if (!this.observations.contains(observation)) {
            this.observations.add(observation);
            observation.setTimeRecord(this);
        }
    }
}
