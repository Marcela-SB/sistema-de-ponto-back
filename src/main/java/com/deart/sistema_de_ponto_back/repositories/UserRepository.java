package com.deart.sistema_de_ponto_back.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.deart.sistema_de_ponto_back.enums.UserRole;
import com.deart.sistema_de_ponto_back.models.User;
import com.deart.sistema_de_ponto_back.repositories.base.BaseRepository;

public interface UserRepository extends BaseRepository<User, Long> {
    
    Optional<User> findByExternalIdAndActiveTrue(UUID externalId);

    Optional<User> findByEmailAndActiveTrue(String email);

    List<User> findAllByActiveTrue();
    
    boolean existsByEmail(String email);

    boolean existsByEmailAndExternalIdNot(String email, UUID externalId);
    
    Optional<User> findByCpf(String cpf);

    Optional<User> findByCpfAndActiveTrue(String cpf);

    boolean existsByCpfAndExternalIdNot(String cpf, UUID externalId);

    boolean existsByCpf(String cpf);

    List<User> findAllByRole(UserRole role);
}
