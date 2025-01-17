package com.empresa.mspersonclient.application;

import com.empresa.mspersonclient.application.exceptions.ClientNotFoundException;
import com.empresa.mspersonclient.domain.Client;
import com.empresa.mspersonclient.domain.events.ClientCreatedEvent;
import com.empresa.mspersonclient.domain.events.ClientDeletedEvent;
import com.empresa.mspersonclient.domain.events.ClientUpdatedEvent;
import com.empresa.mspersonclient.domain.ports.ClientRepositoryPort;
import com.empresa.mspersonclient.domain.ports.EventPublisherPort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientApplicationService {

    private final ClientRepositoryPort repositoryPort;
    private final EventPublisherPort eventPublisher;

    public ClientApplicationService(ClientRepositoryPort repositoryPort, EventPublisherPort eventPublisher) {
        this.repositoryPort = repositoryPort;
        this.eventPublisher = eventPublisher;
    }

    public List<Client> findAll() {
        return repositoryPort.find();
    }

    public Client getClientById(String id) {
        return repositoryPort.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Cliente no encontrado: " + id));
    }

    public Client createClient(Client client) {
        Client savedClient = repositoryPort.save(client);
        eventPublisher.publish(new ClientCreatedEvent(savedClient.getClienteId(), savedClient.getNombre()));
        return savedClient;
    }

    public Client updateClient(String id, Client clientDetails) {
        Client existingClient = getClientById(id);
        existingClient.setNombre(clientDetails.getNombre());
        existingClient.setEdad(clientDetails.getEdad());
        existingClient.setTelefono(clientDetails.getTelefono());
        existingClient.setDireccion(clientDetails.getDireccion());
        repositoryPort.save(existingClient);
        eventPublisher.publish(new ClientUpdatedEvent(existingClient.getClienteId(), existingClient.getNombre()));
        return existingClient;
    }

    public String deleteClient(String id) {
        Client client = getClientById(id);
        repositoryPort.delete(id);
        eventPublisher.publish(new ClientDeletedEvent(client.getClienteId()));
        return "Eliminado satisfactoriamente";
    }

}