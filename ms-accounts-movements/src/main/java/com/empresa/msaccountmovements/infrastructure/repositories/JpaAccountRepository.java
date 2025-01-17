package com.empresa.msaccountmovements.infrastructure.repositories;

import com.empresa.msaccountmovements.domain.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaAccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByNumeroCuenta(String numeroCuenta);
    Optional<List<Account>> findByClienteId(String clientId);
}
