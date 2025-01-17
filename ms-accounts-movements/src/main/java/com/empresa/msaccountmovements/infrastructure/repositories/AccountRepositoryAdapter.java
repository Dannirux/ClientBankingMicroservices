package com.empresa.msaccountmovements.infrastructure.repositories;

import com.empresa.msaccountmovements.domain.models.Account;
import com.empresa.msaccountmovements.domain.ports.AccountRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Component
public class AccountRepositoryAdapter implements AccountRepositoryPort {

    private final JpaAccountRepository jpaAccountRepository;

    public AccountRepositoryAdapter(JpaAccountRepository jpaRepository) {
        this.jpaAccountRepository = jpaRepository;
    }
    @Override
    public Optional<Account> findByNumeroCuenta(String numeroCuenta) {
        return jpaAccountRepository.findByNumeroCuenta(numeroCuenta);
    }

    @Override
    public Optional<List<Account>> findByClienteId(String clienteId) {
        return jpaAccountRepository.findByClienteId(clienteId);
    }

    @Override
    public Optional<Account> findById(String id) {
        return jpaAccountRepository.findById(id);
    }

    @Override
    public List<Account> findAll() {
        return jpaAccountRepository.findAll();
    }

    @Override
    public Account save(Account account) {
        return jpaAccountRepository.save(account);
    }

    @Override
    public Account update(Account account) {
        return jpaAccountRepository.save(account);
    }

    @Override
    public void deleteById(String id) {
        jpaAccountRepository.deleteById(id);
    }
}
