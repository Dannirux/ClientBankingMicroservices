package com.empresa.mspersonclient.unit.application;

import com.empresa.mspersonclient.application.exceptions.ClientNotFoundException;
import com.empresa.mspersonclient.domain.Client;
import com.empresa.mspersonclient.domain.ports.ClientRepositoryPort;
import com.empresa.mspersonclient.application.ClientApplicationService;
import com.empresa.mspersonclient.domain.ports.EventPublisherPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas para ClientApplicationService")
public class ClientApplicationServiceTest {

    @Mock
    private ClientRepositoryPort clientRepository;

    @Mock
    private EventPublisherPort eventPublisher;

    @InjectMocks
    private ClientApplicationService clientService;

    private Client validClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validClient = new Client(
                "John Doe", "Male", 30, "1234567890", "123 Main St", "123456789",
                "C001", "password123", true
        );
    }

    @Nested
    @DisplayName("Pruebas para el método getClientById")
    class GetClientByIdTests {

        @Test
        @DisplayName("Debería retornar un cliente existente")
        public void testGetClientById_Success() {
            when(clientRepository.findById("C001")).thenReturn(Optional.of(validClient));
            Client result = clientService.getClientById("C001");
            assertNotNull(result);
            assertEquals("John Doe", result.getNombre());
            assertEquals("C001", result.getClienteId());
            verify(clientRepository, times(1)).findById("C001");
        }

        @Test
        @DisplayName("Debería lanzar una excepción si el cliente no existe")
        public void testGetClientById_NotFound() {
            when(clientRepository.findById("C999")).thenReturn(Optional.empty());
            Exception exception = assertThrows(ClientNotFoundException.class, () -> {
                clientService.getClientById("C999");
            });
            verify(clientRepository, times(1)).findById("C999");
        }
    }

    @Nested
    @DisplayName("Pruebas para el método createClient")
    class CreateClientTests {

        @Test
        @DisplayName("Debería crear un cliente exitosamente")
        public void testCreateClient_Success() {
            when(clientRepository.save(any())).thenReturn(validClient);
            doNothing().when(eventPublisher).publish(any());

            Client result = clientService.createClient(validClient);
            assertNotNull(result);
            assertEquals("John Doe", result.getNombre());
            assertEquals("C001", result.getClienteId());
            verify(clientRepository, times(1)).save(validClient);
            verify(eventPublisher, times(1)).publish(any());
            verify(eventPublisher, times(1)).publish(any());
        }

        @Test
        @DisplayName("Debería lanzar una excepción si los datos del cliente son inválidos")
        public void testCreateClient_InvalidData() {
            Client invalidClient = new Client(
                    "", "Male", 30, "1234567890", "123 Main St", "123456789",
                    "C001", "password123", true
            );
            assertThrows(NullPointerException.class, () -> {
                clientService.createClient(invalidClient);
            });
        }
    }

    @Nested
    @DisplayName("Pruebas para el método deleteClient")
    class DeleteClientTests {

        @Test
        @DisplayName("Debería eliminar un cliente exitosamente")
        public void testDeleteClient_Success() {
            when(clientRepository.findById("C001")).thenReturn(Optional.of(validClient));
            doNothing().when(clientRepository).delete("C001");
            doNothing().when(eventPublisher).publish(any());

            String result = clientService.deleteClient("C001");

            assertNotNull(result);
            assertEquals("Eliminado satisfactoriamente", result);
            verify(clientRepository, times(1)).findById("C001");
            verify(clientRepository, times(1)).delete("C001");
            verify(eventPublisher, times(1)).publish(any());
        }

        @Test
        @DisplayName("Debería lanzar una excepción si el cliente no existe")
        public void testDeleteClient_NotFound() {
            when(clientRepository.findById("C999")).thenReturn(Optional.empty());
            Exception exception = assertThrows(ClientNotFoundException.class, () -> {
                clientService.deleteClient("C999");
            });
            assertEquals("Cliente no encontrado: C999", exception.getMessage());
            verify(clientRepository, times(1)).findById("C999");
            verify(clientRepository, never()).delete(any());
            verify(eventPublisher, never()).publish(any());
        }
    }
}
