package com.empresa.mspersonclient.domain.ports;

import com.empresa.mspersonclient.domain.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepositoryPort {
    Optional<Client> findById(String id);
    List<Client> find();
    Client save(Client client);
    void delete(String id);
}
