package com.empresa.msaccountmovements.application;

import com.empresa.msaccountmovements.application.exceptions.ClientNotFoundException;
import com.empresa.msaccountmovements.domain.events.GetClientEvent;
import com.empresa.msaccountmovements.domain.models.Account;
import com.empresa.msaccountmovements.domain.dtos.AccountRequestDto;
import com.empresa.msaccountmovements.domain.dtos.AccountResponseDto;
import com.empresa.msaccountmovements.domain.dtos.ClientDto;
import com.empresa.msaccountmovements.domain.ports.AccountRepositoryPort;
import com.empresa.msaccountmovements.domain.ports.ClientServicePort;
import com.empresa.msaccountmovements.domain.ports.EventPublisherPort;
import com.empresa.msaccountmovements.infrastructure.adapters.ClientAsyncAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountMovementApplicationService {

    private final AccountRepositoryPort accountRepository;
    private final ClientServicePort clientServicePort;
    private final EventPublisherPort eventPublisherPort;
    private final AccountNumberGenerator accountNumberGenerator;

    public AccountMovementApplicationService(
            AccountRepositoryPort accountRepository,
            @Qualifier("rabbit") ClientServicePort clientServicePort,
            EventPublisherPort eventPublisherPort, AccountNumberGenerator accountNumberGenerator) {
        this.accountRepository = accountRepository;
        this.clientServicePort = clientServicePort;
        this.eventPublisherPort = eventPublisherPort;
        this.accountNumberGenerator = accountNumberGenerator;
    }

    public List<AccountResponseDto> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(account -> {
                    publishClientEventIfRabbit(account.getClienteId());
                    ClientDto cliente = clientServicePort.getClientById(account.getClienteId())
                            .orElseThrow(() -> new ClientNotFoundException("Cliente no encontrado"));
                    return buildAccountResponseDTO(account, cliente);
                })
                .collect(Collectors.toList());
    }

    public AccountResponseDto findOne(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        publishClientEventIfRabbit(account.getClienteId());
        ClientDto cliente = clientServicePort.getClientById(account.getClienteId())
                .orElseThrow(() -> new ClientNotFoundException("Cliente no encontrado"));

        return buildAccountResponseDTO(account, cliente);
    }

    public AccountResponseDto createAccount(AccountRequestDto requestDTO) {
        publishClientEventIfRabbit(requestDTO.getClienteId());
        ClientDto client = clientServicePort.getClientById(requestDTO.getClienteId())
                .orElseThrow(() -> new ClientNotFoundException("Cliente no encontrado"));

        Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setNumeroCuenta(accountNumberGenerator.generate(requestDTO.getTipoCuenta()));
        account.setTipoCuenta(requestDTO.getTipoCuenta());
        account.setSaldoInicial(requestDTO.getSaldoInicial());
        account.setEstado(requestDTO.getEstado());
        account.setClienteId(client.getId());
        Account savedAccount = accountRepository.save(account);

        return buildAccountResponseDTO(savedAccount, client);
    }

    public AccountResponseDto update(String accountId, AccountRequestDto requestDTO) {
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        publishClientEventIfRabbit(requestDTO.getClienteId());
        ClientDto client = clientServicePort.getClientById(requestDTO.getClienteId())
                .orElseThrow(() -> new ClientNotFoundException("Cliente no encontrado"));

        existingAccount.setEstado(requestDTO.getEstado());

        Account updatedAccount = accountRepository.save(existingAccount);

        return buildAccountResponseDTO(updatedAccount, client);
    }

    public void deleteAccount(String id) {
        accountRepository.deleteById(id);
    }

    private AccountResponseDto buildAccountResponseDTO(Account account, ClientDto cliente) {
        return new AccountResponseDto(
                account.getId(),
                account.getNumeroCuenta(),
                account.getTipoCuenta(),
                account.getSaldoInicial(),
                account.getEstado(),
                cliente
        );
    }

    private void publishClientEventIfRabbit(String clientId) {
        if (clientServicePort instanceof ClientAsyncAdapter) {
            eventPublisherPort.publish(new GetClientEvent(clientId, ""));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
