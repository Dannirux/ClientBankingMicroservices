package com.empresa.msaccountmovements.domain.ports;

import com.empresa.msaccountmovements.domain.models.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepositoryPort {
    Optional<Account> findByNumeroCuenta(String numeroCuenta);
    Optional<List<Account>> findByClienteId (String clienteId);
    Optional<Account> findById(String id);
    List<Account> findAll();
    Account save(Account account);
    Account update(Account account);
    void deleteById(String id);
}