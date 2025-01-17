package com.empresa.mspersonclient.domain.ports;

import com.empresa.mspersonclient.domain.Client;

import java.util.List;

public interface ClientControllerPort {
    List<Client> getAll();
    Client getClient(String id);
    Client createClient(Client client);
    Client updateClient(String id, Client client);
    String deleteClient(String id);
}