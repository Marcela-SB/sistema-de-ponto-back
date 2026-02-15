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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "time_records")
@Getter
@Setter
@NoArgsConstructor
public class TimeRecord extends AuditableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Duration totalHours;

    @OneToMany(mappedBy = "timeRecord", cascade = CascadeType.ALL)
    private List<TimeRecordObservation> observations = new ArrayList<>();



    public String getTotalHoursFormatted() {
        if (totalHours == null) return "00:00";
        long s = totalHours.abs().getSeconds();
        return String.format("%02d:%02d", s / 3600, (s % 3600) / 60);
    }
}
