package com.empresa.mspersonclient.domain.services;

import com.empresa.mspersonclient.application.exceptions.ClientInactiveException;
import com.empresa.mspersonclient.domain.Client;

public class ClientDomainService {
    public void validateClientCanBeDeleted(Client client) {
        if (!client.isEstado()) {
            throw new ClientInactiveException("No se puede eliminar un cliente inactivo.");
        }
    }
}
