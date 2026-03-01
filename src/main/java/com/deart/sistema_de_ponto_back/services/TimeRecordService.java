package com.deart.sistema_de_ponto_back.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deart.sistema_de_ponto_back.dtos.requests.ObservationInput;
import com.deart.sistema_de_ponto_back.dtos.requests.TimeRecordManualRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.TimeRecordUpdateRequest;
import com.deart.sistema_de_ponto_back.enums.ObservationType;
import com.deart.sistema_de_ponto_back.exceptions.domain.DailyRecordLimitExceededException;
import com.deart.sistema_de_ponto_back.exceptions.domain.DailyWorkdayAlreadyClosedException;
import com.deart.sistema_de_ponto_back.exceptions.domain.InactiveUserException;
import com.deart.sistema_de_ponto_back.exceptions.domain.TimeRecordNotFoundException;
import com.deart.sistema_de_ponto_back.mappers.TimeRecordMapper;
import com.deart.sistema_de_ponto_back.models.Intern;
import com.deart.sistema_de_ponto_back.models.TimeRecord;
import com.deart.sistema_de_ponto_back.repositories.TimeRecordRepository;


@Service
public class TimeRecordService {
    private final TimeRecordRepository recordRepository;
    private final InternService internService;
    private final ObservationService observationService;
    private final TimeRecordMapper recordMapper;

    public TimeRecordService(TimeRecordRepository recordRepository, InternService internService, ObservationService observationService, TimeRecordMapper recordMapper){
        this.recordRepository = recordRepository;
        this.internService = internService;
        this.observationService = observationService;
        this.recordMapper = recordMapper;
    }


    /**
     * Busca um registro utilizando o {@code externalId}.
     * 
     * @param externalId {@code UUID} do registro.
     * @return O {@link TimeRecord} encontrado.
     * @throws TimeRecordNotFoundException caso não seja encontrado.
     */
    public TimeRecord findByExternalId(UUID externalId){
        return recordRepository.findByExternalId(externalId)
                .orElseThrow(TimeRecordNotFoundException::new);
    }

    /**
     * Busca todos os registros no banco de dados.
     * 
     * @return Uma lista (não ordenada) contendo os registros.
     */
    public List<TimeRecord> findAll(){
        return recordRepository.findAll();
    }

    /**
     * Busca todos os registros de um bolsista.
     * 
     * @param externalId
     * @return Uma lista ordenada cronologicamente.
     */
    public List<TimeRecord> findAllByIntern(UUID externalId){
        return recordRepository.findHistoryByIntern(externalId);
    }

    /**
     * Busca os registros de todos os bolsistas feitos hoje.
     * 
     * @return Lista com os registros ordenados por hora de entrada ({@code clockIn})
     */
    public List<TimeRecord> findAllToday(){
        LocalDate today = LocalDate.now();
        return recordRepository.findDayRecords(today);
    }

    /**
     * Busca o registro de hoje de um bolsista específico.
     * 
     * @param externalId Id externo do bolsista.
     * @return Um {@link Optional} contendo o registro de ponto, se encontrado.
     */
    public Optional<TimeRecord> findByInternToday(UUID externalId){
        LocalDate today = LocalDate.now();
        return recordRepository.findByInternExternalIdAndRecordDate(externalId, today);
    }

    /**
     * Busca os registros do ano atual de um bolsista específico.
     * 
     * @param externalId Id externo do bolsista.
     * @return Lista dos registros do bolsista no ano vigente ordenados cronologicamente.
     */
    public List<TimeRecord> findAllByInternInThisYear(UUID externalId){
        LocalDate today = LocalDate.now();
        LocalDate beginOfYear = today.withDayOfYear(1);

        return recordRepository.findByInternInPeriod(externalId, beginOfYear, today);
    }

    /**
     * Busca todos os registros de um bolsista dentro de um período de tempo.
     * 
     * @param externalId Id externo do bolsista.
     * @param startDate Data de início.
     * @param endDate Data de fim.
     * @return Uma Lista de registros ordenados cronologicamente.
     */
    public List<TimeRecord> findAllByInternInPeriod(UUID externalId, YearMonth startMonth, YearMonth endMonth) {
        LocalDate start = startMonth.atDay(1); 
        LocalDate end = endMonth.atEndOfMonth(); 

        return recordRepository.findByInternInPeriod(externalId, start, end);
    }

    /**
     * Valida se o objeto de entrada da observação e seu conteúdo textual são válidos.
     * <p>
     * A observação é considerada válida se não for nula e se o texto contido nela não estiver nulo nem em branco (vazio ou apenas com espaços).
     * </p>
     *
     * @param obs O objeto {@link ObservationInput} a ser validado.
     * @return {@code true} se a observação for válida; {@code false} caso contrário.
     */
    private boolean isObservationValid(ObservationInput obs) {
        return obs != null && obs.text() != null && !obs.text().isBlank();
    }

    /**
     * Registra o ponto de entrada ou saída em tempo real (Batida de Ponto).
     * <p>
     * Se não houver registro para o dia atual, cria um novo com o horário de entrada ({@code clockIn}).
     * Se já houver um registro sem horário de saída, atualiza o horário de saída ({@code clockOut}).
     * </p>
     * 
     * @param internExternalId {@code UUID} do bolsista.
     * @param obs DTO com texto da observação do bolsista (opcional).
     * @return O registro de ponto {@link TimeRecord} atualizado ou criado.
     * @throws InactiveUserException se o bolsista estiver desativado.
     * @throws DailyWorkdayAlreadyClosedException se o ponto de entrada e saída já estiverem preenchidos.
     */
    // verificar se o Intern pertence a quem está logado.
    @Transactional
    public TimeRecord register(UUID internExternalId, ObservationInput obs){
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now().withNano(0);

        Intern intern = internService.validateAndGetIntern(internExternalId);

        TimeRecord registry = recordRepository.findByInternAndRecordDate(intern, today)
            .map(record -> {
                if (record.getClockOut() != null) {
                    throw new DailyWorkdayAlreadyClosedException();
                }
                record.setClockOut(now);
                return record;
            })
            .orElseGet(() -> {
                TimeRecord newRecord = new TimeRecord();
                newRecord.setIntern(intern);
                newRecord.setRecordDate(today);
                newRecord.setClockIn(now);
                return newRecord;
            });

        if (isObservationValid(obs)) {
            observationService.processObservationFromRecord(registry, ObservationType.INTERN, obs);
        }

        TimeRecord savedRecord = recordRepository.save(registry);
        savedRecord.calculateTotalHours();    
        return savedRecord;
    }

    /**
     * Registra manualmente um ponto retroativo ou específico.
     * <p>
     * Utilizado por supervisores ou administradores para lançar registros que não foram realizados em tempo real. Verifica se já existe um registro para o bolsista na data informada.
     * </p>
     * 
     * @param request DTO contendo os dados do registro (ID do bolsista, data, horários e observação).
     * @return O registro de ponto {@link TimeRecord} persistido.
     * @throws InactiveUserException se o bolsista estiver desativado.
     * @throws DailyRecordLimitExceededException se já existir um registro para a data informada.
     */
    @Transactional
    public TimeRecord manualRegister(TimeRecordManualRequest request){
        Intern intern = internService.validateAndGetIntern(request.internExternalId());

        boolean registryExists = recordRepository.existsByInternAndRecordDate(intern, request.recordDate());
        if (registryExists) {
            throw new DailyRecordLimitExceededException(request.recordDate());
        }

        TimeRecord record = recordMapper.toEntity(request, intern);
        record.calculateTotalHours();

        if (isObservationValid(request.supervisorObservation())) {
            observationService.processObservationFromRecord(record, ObservationType.SUPERVISOR, request.supervisorObservation());
        }

        return recordRepository.save(record);
    }
    
    /**
     * Atualiza um registro de ponto existente.
     * <p>
     * Permite a alteração dos horários de entrada, saída e a inclusão/edição da observação do supervisor.
     * Apenas registros de bolsistas ativos podem ser editados.
     * </p>
     * 
     * @param externalId {@code UUID} único do registro de ponto a ser atualizado.
     * @param updateRequest DTO com os novos dados de horário e observação.
     * @return O registro {@link TimeRecord} com as alterações aplicadas.
     * @throws InactiveUserException se o bolsista dono do registro estiver desativado.
     * @throws EntityNotFoundException se o registro não for encontrado.
     */
    @Transactional
    public TimeRecord update(UUID externalId, TimeRecordUpdateRequest updateRequest){
        TimeRecord record = findByExternalId(externalId);
        
        internService.validateInternIsActive(record.getIntern());

        if (updateRequest.clockIn() != null && !Objects.equals(record.getClockIn(), updateRequest.clockIn())) {
            record.setClockIn(updateRequest.clockIn());
        }

        if (updateRequest.clockOut() != null && !Objects.equals(record.getClockOut(), updateRequest.clockOut())) {
            record.setClockOut(updateRequest.clockOut());
        }

        record.calculateTotalHours();

        if (isObservationValid(updateRequest.supervisorObservation())) {
            observationService.processObservationFromRecord(record, ObservationType.SUPERVISOR, updateRequest.supervisorObservation());
        }

        return recordRepository.save(record);
    }

    /**
     * Remove permanentemente um registro de ponto do sistema.
     * <p>
     * Utilizada apenas por Supervisores ou Administradores.
     * </p>
     * 
     * @param externalId UUID único do registro a ser excluído.
     * @throws EntityNotFoundException se o registro não for encontrado.
     */
    @Transactional
    public void delete(UUID externalId){
        TimeRecord record = findByExternalId(externalId);
        recordRepository.delete(record);
    }


    /**
     * Retorna a lista de anos disponíveis para filtro no frontend.
     * 
     * @return Lista de anos em ordem decrescente.
     */
    @Transactional(readOnly = true)
    public List<Integer> getAvailableYears() {
        return recordRepository.findDistinctYears();
    }
}
