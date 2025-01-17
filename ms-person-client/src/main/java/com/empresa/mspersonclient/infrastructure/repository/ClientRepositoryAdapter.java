package com.empresa.mspersonclient.infrastructure.repository;

import com.empresa.mspersonclient.domain.Client;
import com.empresa.mspersonclient.domain.ports.ClientRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ClientRepositoryAdapter implements ClientRepositoryPort {
    private final JpaClientRepository jpaRepository;

    public ClientRepositoryAdapter(JpaClientRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Client> findById(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Client> find() {
        return jpaRepository.findAll();
    }

    @Override
    public Client save(Client client) {
        return jpaRepository.save(client);
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
