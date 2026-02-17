package com.deart.sistema_de_ponto_back.mappers;

import java.time.format.DateTimeFormatter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.deart.sistema_de_ponto_back.dtos.requests.ObservationInput;
import com.deart.sistema_de_ponto_back.dtos.requests.ObservationRequest;
import com.deart.sistema_de_ponto_back.dtos.responses.ObservationResponse;
import com.deart.sistema_de_ponto_back.enums.ObservationType;
import com.deart.sistema_de_ponto_back.models.Observation;
import com.deart.sistema_de_ponto_back.models.TimeRecord;

@Mapper(componentModel = "spring")
public interface ObservationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "timeRecord", source = "record")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "text", source = "text")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Observation toEntity(TimeRecord record, ObservationType type, String text);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "timeRecord", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "text", source = "text")
    void updateEntityFromText(@MappingTarget Observation observation, String text);

    @Mapping(target = "externalId", source = "externalId")
    @Mapping(target = "timeRecordExternalId", source = "timeRecord.externalId")
    @Mapping(target = "lastUpdate", source = "updatedAt", qualifiedByName = "formatDateTime")
    ObservationResponse toResponse(Observation observation);


    @Named("formatDateTime")
    default String formatDateTime(java.time.LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
