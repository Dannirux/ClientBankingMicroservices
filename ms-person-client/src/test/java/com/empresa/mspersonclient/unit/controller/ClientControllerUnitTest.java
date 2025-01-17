package com.empresa.mspersonclient.unit.controller;

import com.empresa.mspersonclient.application.ClientApplicationService;
import com.empresa.mspersonclient.domain.Client;
import com.empresa.mspersonclient.infrastructure.controller.ClientController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas para ClientController")
public class ClientControllerUnitTest {

    @Mock
    private ClientApplicationService clientService;

    @InjectMocks
    private ClientController clientController;

    private Client validClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inicialización de un cliente válido para reutilizar en las pruebas
        validClient = new Client(
                "John Doe", "Male", 30, "1234567890", "123 Main St", "123456789",
                "C001", "password123", true
        );
    }

    @Nested
    @DisplayName("Pruebas para el endpoint GET /clients/{id}")
    class GetClientByIdTests {

        @Test
        @DisplayName("Debería retornar un cliente existente")
        public void testGetClientById_Success() {
            when(clientService.getClientById("1")).thenReturn(validClient);

            Client result = clientController.getClient("1");

            assertNotNull(result);
            assertEquals("John Doe", result.getNombre());
            assertEquals("Male", result.getGenero());
            assertEquals(30, result.getEdad());
        }

        @Test
        @DisplayName("Debería lanzar una excepción si el cliente no existe")
        public void testGetClientById_NotFound() {
            when(clientService.getClientById("999"))
                    .thenThrow(new RuntimeException("Client not found"));

            Exception exception = assertThrows(RuntimeException.class, () -> {
                clientController.getClient("999");
            });

            assertEquals("Client not found", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Pruebas para el endpoint GET /clients")
    class GetAllClientsTests {

        @Test
        @DisplayName("Debería retornar una lista de clientes")
        public void testGetAllClients_Success() {
            List<Client> clients = Arrays.asList(validClient, new Client(
                    "Jane Doe", "Female", 28, "9876543210", "456 Main St", "987654321",
                    "C002", "password1234", true
            ));
            when(clientService.findAll()).thenReturn(clients);

            List<Client> result = clientController.getAll();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("John Doe", result.get(0).getNombre());
            assertEquals("Jane Doe", result.get(1).getNombre());
        }

        @Test
        @DisplayName("Debería retornar una lista vacía cuando no hay clientes")
        public void testGetAllClients_EmptyList() {
            when(clientService.findAll()).thenReturn(List.of());

            List<Client> result = clientController.getAll();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Pruebas para el endpoint POST /clients")
    class CreateClientTests {

        @Test
        @DisplayName("Debería crear un cliente exitosamente")
        public void testCreateClient_Success() {
            when(clientService.createClient(any())).thenReturn(validClient);

            Client result = clientController.createClient(validClient);

            assertNotNull(result);
            assertEquals("John Doe", result.getNombre());
            assertTrue(result.isEstado());
        }

        @Test
        @DisplayName("Debería lanzar una excepción al crear un cliente con datos inválidos")
        public void testCreateClient_InvalidData() {
            when(clientService.createClient(any()))
                    .thenThrow(new IllegalArgumentException("Invalid client data"));

            Client invalidClient = new Client(
                    "", "Male", 30, "1234567890", "123 Main St", "123456789",
                    "C001", "password123", true
            );

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                clientController.createClient(invalidClient);
            });

            assertEquals("Invalid client data", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Pruebas para el endpoint DELETE /clients/{id}")
    class DeleteClientTests {

        @Test
        @DisplayName("Debería eliminar un cliente exitosamente")
        public void testDeleteClient_Success() {
            when(clientService.deleteClient("1")).thenReturn("Client successfully deleted");

            String result = clientController.deleteClient("1");

            verify(clientService, times(1)).deleteClient("1");

            assertNotNull(result);
            assertEquals("Client successfully deleted", result);
        }

        @Test
        @DisplayName("Debería lanzar una excepción si el cliente no existe")
        public void testDeleteClient_NotFound() {
            doThrow(new RuntimeException("Client not found"))
                    .when(clientService).deleteClient("999");

            Exception exception = assertThrows(RuntimeException.class, () -> {
                clientController.deleteClient("999");
            });

            assertEquals("Client not found", exception.getMessage());
        }
    }
}
