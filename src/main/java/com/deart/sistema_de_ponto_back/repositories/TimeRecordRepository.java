package com.deart.sistema_de_ponto_back.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deart.sistema_de_ponto_back.models.Intern;
import com.deart.sistema_de_ponto_back.models.TimeRecord;
import com.deart.sistema_de_ponto_back.repositories.base.AuditableRepository;

/**
 * Repositório para gerenciar a persistência dos registros de ponto ({@link TimeRecord}).
 * <p>
 * Esta interface estende {@link AuditableRepository} para fornecer operações CRUD básicas e funcionalidades de auditoria, além de consultas customizadas baseadas em datas e bolsistas.
 * </p>
 */
public interface TimeRecordRepository extends AuditableRepository<TimeRecord>{
    /**
     * Busca um registro de ponto específico de um bolsista em uma determinada data.
     * 
     * @param externalId O {@code UUID} externo do bolsista.
     * @param recordDate A data do registro buscado.
     * @return Um {@link Optional} contendo o registro de ponto, se encontrado.
     */
    Optional<TimeRecord> findByInternExternalIdAndRecordDate(UUID externalId, LocalDate recordDate);


    /**
     * Busca um registro de ponto de um bolsista específico utilizando a instância do objeto e a data.
     * 
     * @param intern A entidade {@link Intern} associada.
     * @param recordDate A data do registro buscado.
     * @return Um {@link Optional} contendo o registro de ponto, se encontrado.
     */
    Optional<TimeRecord> findByInternAndRecordDate(Intern intern, LocalDate recordDate);


    /**
     * Verifica a existência de um registro de ponto para um bolsista em uma data específica.
     * @param intern A entidade {@link Intern} associada.
     * @param recordDate A data do registro buscado.
     * @return {@code true} se houver um registro para o bolsista na data informada; {@code false} caso contrário.
     */
    boolean existsByInternAndRecordDate(Intern intern, LocalDate recordDate);


    /**
     * Retorna o histórico completo de registros de ponto de um bolsista.
     * 
     * @param externalId O {@code UUID} externo do bolsista.
     * @return Lista de {@link TimeRecord} ordenada da data mais recente para a mais antiga.
     */
    @Query("SELECT t FROM TimeRecord t WHERE t.intern.externalId = :id ORDER BY t.recordDate DESC")
    List<TimeRecord> findHistoryByIntern(@Param("id") UUID externalId);


    /**
     * Busca todos os registros de ponto realizados em uma data específica, ordenados pelo horário de entrada.
     * 
     * @param recordDate A data para filtragem.
     * @return Lista de registros ordenados pelo {@code clockIn} (ascendente).
     */
    @Query("SELECT t FROM TimeRecord t WHERE t.recordDate = :date ORDER BY t.clockIn ASC")
    List<TimeRecord> findDayRecords(@Param("date") LocalDate recordDate);
    

    /**
     * Retorna todos os registros de ponto dentro de um intervalo de datas específico.
     * 
     * @param startDate Data inicial do intervalo (inclusive).
     * @param endDate   Data final do intervalo (inclusive).
     * @return Lista de registros ordenados cronologicamente.
     */
    @Query("SELECT t FROM TimeRecord t WHERE t.recordDate BETWEEN :start AND :end ORDER BY t.recordDate ASC")
    List<TimeRecord> findInPeriod(@Param("start") LocalDate startDate, @Param("end") LocalDate endDate);


    /**
     * Busca os registros de ponto de um bolsista específico dentro de um intervalo de datas.
     * 
     * @param externalId O {@code UUID} externo do bolsista.
     * @param startDate  Data inicial do intervalo.
     * @param endDate    Data final do intervalo.
     * @return Lista de registros do bolsista ordenados cronologicamente.
     */
    @Query("SELECT t FROM TimeRecord t WHERE t.intern.externalId = :id AND t.recordDate BETWEEN :start AND :end ORDER BY t.recordDate ASC")
    List<TimeRecord> findByInternInPeriod(@Param("id") UUID externalId, @Param("start") LocalDate startDate, @Param("end") LocalDate endDate);


    /**
     * Busca registros de ponto de um bolsista específico que ainda não possuem horário de saída registrado.
     * Útil para identificar jornadas em aberto.
     * 
     * @param externalId O {@code UUID} externo do bolsista.
     * @return Lista de registros onde o {@code clockOut} é nulo.
     */
    @Query("SELECT t FROM TimeRecord t WHERE t.intern.externalId = :id AND t.clockOut IS NULL")
    List<TimeRecord> findOpenRecordsByIntern(@Param("id") UUID externalId);
}
