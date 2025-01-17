package com.empresa.mspersonclient.integration;

import com.empresa.mspersonclient.domain.Client;
import com.empresa.mspersonclient.infrastructure.repository.JpaClientRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Pruebas de integración para Client")
public class ClientIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JpaClientRepository clientRepository;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/clients";
        clientRepository.deleteAll();
    }

    @AfterEach
    public void tearDown() {
        clientRepository.deleteAll();
    }

    @Test
    @DisplayName("Debería eliminar un cliente exitosamente")
    public void testDeleteClient() {
        Client savedClient = clientRepository.save(new Client(
                "Jane Doe", "Femenino", 25, "9876543210", "456 Elm St", "1234567890",
                null, "password456", true
        ));

        String url = baseUrl + "/" + savedClient.getClienteId();

        restTemplate.delete(url);

        Optional<Client> deletedClient = clientRepository.findById(savedClient.getClienteId());
        assertTrue(deletedClient.isEmpty());
    }

}
