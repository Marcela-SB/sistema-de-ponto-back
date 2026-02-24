package com.deart.sistema_de_ponto_back.repositories;

import com.deart.sistema_de_ponto_back.models.Intern;
import com.deart.sistema_de_ponto_back.models.User;
import com.deart.sistema_de_ponto_back.repositories.base.BaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InternRepository extends BaseRepository<Intern, Long> {
    
    Optional<Intern> findByEnrollmentNumber(String enrollmentNumber); 

    List<Intern> findAllBySupervisor(User supervisor);
    
    List<Intern> findAllBySupervisorExternalId(UUID externalId);

    Optional<Intern> findByUser(User user);

    Optional<Intern> findByUserExternalId(UUID externalId);

    List<Intern> findAllByUserActiveTrue();

    boolean existsByEnrollmentNumber(String enrollmentNumber);

    boolean existsByEnrollmentNumberAndUserExternalIdNot(String enrollmentNumber, UUID externalId);
}
