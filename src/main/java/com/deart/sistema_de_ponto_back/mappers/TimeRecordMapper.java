package com.deart.sistema_de_ponto_back.mappers;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.deart.sistema_de_ponto_back.dtos.requests.TimeRecordManualRequest;
import com.deart.sistema_de_ponto_back.dtos.responses.ObservationResponse;
import com.deart.sistema_de_ponto_back.dtos.responses.TimeRecordResponse;
import com.deart.sistema_de_ponto_back.enums.ObservationType;
import com.deart.sistema_de_ponto_back.models.Intern;
import com.deart.sistema_de_ponto_back.models.Observation;
import com.deart.sistema_de_ponto_back.models.TimeRecord;

@Mapper(
    componentModel = "spring", 
    uses = {ObservationMapper.class},
    imports = {com.deart.sistema_de_ponto_back.enums.ObservationType.class}
)
public abstract class TimeRecordMapper {

    @Autowired
    protected ObservationMapper observationMapper;


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "intern", source = "intern")
    @Mapping(target = "observations", ignore = true)
    @Mapping(target = "totalHours", ignore = true)
    public abstract TimeRecord toEntity(TimeRecordManualRequest request, Intern intern);

    @Mapping(target = "internExternalId", source = "intern.externalId")
    @Mapping(target = "recordDate", source = "recordDate", qualifiedByName = "formatDate")
    @Mapping(target = "clockIn", source = "clockIn", qualifiedByName = "formatTime")
    @Mapping(target = "clockOut", source = "clockOut", qualifiedByName = "formatTime")
    @Mapping(target = "totalHours", expression = "java(entity.getTotalHoursFormatted())")
    @Mapping(target = "internObservation", expression = "java(filterObs(entity.getObservations(), ObservationType.INTERN))")
    @Mapping(target = "supervisorObservation", expression = "java(filterObs(entity.getObservations(), ObservationType.SUPERVISOR))")
    public abstract TimeRecordResponse toResponse(TimeRecord entity);


    // Métodos auxiliares de formatação
    @Named("formatDate")
    protected String formatDate(java.time.LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
    }

    @Named("formatTime")
    protected String formatTime(java.time.LocalTime time) {
        return time != null ? time.format(DateTimeFormatter.ofPattern("HH:mm")) : "--:--";
    }

    // Lógica para extrair a observação correta da lista e converter para DTO
    protected ObservationResponse filterObs(List<Observation> observations, ObservationType type) {
        if (observations == null) return null;

        return observations.stream()
            .filter(o -> o.getType() == type)
            .findFirst()
            .map(obs -> observationMapper.toResponse(obs))
            .orElse(null);
    }
}