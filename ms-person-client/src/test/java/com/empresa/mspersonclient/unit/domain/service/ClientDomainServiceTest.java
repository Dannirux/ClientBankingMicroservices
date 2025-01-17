package com.empresa.mspersonclient.unit.domain.service;

import com.empresa.mspersonclient.application.exceptions.ClientInactiveException;
import com.empresa.mspersonclient.domain.Client;
import com.empresa.mspersonclient.domain.services.ClientDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas para ClientDomainService")
public class ClientDomainServiceTest {

    private ClientDomainService domainService;
    private Client activeClient;
    private Client inactiveClient;

    @BeforeEach
    public void setUp() {
        domainService = new ClientDomainService();

        activeClient = new Client(
                "John Doe", "Male", 30, "1234567890", "123 Main St", "123456789",
                "C001", "password123", true
        );

        inactiveClient = new Client(
                "Jane Doe", "Female", 25, "9876543210", "456 Elm St", "987654321",
                "C002", "password456", false
        );
    }

    @Test
    @DisplayName("Debería permitir eliminar un cliente activo")
    public void testValidateClientCanBeDeleted_ActiveClient() {
        assertDoesNotThrow(() -> domainService.validateClientCanBeDeleted(activeClient));
    }

    @Test
    @DisplayName("Debería lanzar excepción al intentar eliminar un cliente inactivo")
    public void testValidateClientCanBeDeleted_InactiveClient() {
        ClientInactiveException exception = assertThrows(ClientInactiveException.class, () -> {
            domainService.validateClientCanBeDeleted(inactiveClient);
        });

        assertEquals("No se puede eliminar un cliente inactivo.", exception.getMessage());
    }
}
