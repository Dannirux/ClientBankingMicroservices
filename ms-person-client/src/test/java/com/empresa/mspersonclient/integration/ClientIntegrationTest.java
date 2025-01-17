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
    }

    @AfterEach
    public void tearDown() {
        clientRepository.deleteAll();
    }

    @Test
    @DisplayName("Debería crear un cliente exitosamente")
    public void testCreateClient() {
        Client newClient = new Client(
                "John Doe", "Masculino", 30, "1234567890", "123 Main St", "9876543210",
                null, "password123", true
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl, newClient, Map.class);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("John Doe", ((Map<String, Object>) response.getBody()).get("nombre"));
    }

    @Test
    @DisplayName("Debería obtener un cliente existente por ID")
    public void testGetClientById() {
        Client savedClient = clientRepository.save(new Client(
                "Jane Doe", "Femenino", 25, "9876543210", "456 Elm St", "1234567890",
                null, "password456", true
        ));

        String url = baseUrl + "/" + savedClient.getClienteId();

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Jane Doe", ((Map<String, Object>) response.getBody()).get("nombre"));
    }

    @Test
    @DisplayName("Debería retornar 500 para validacion erronea")
    public void testGetNonexistentClient() {
        String url = baseUrl + "/999";

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        assertEquals(500, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Cliente no encontrado", response.getBody().get("message"));
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

    @Test
    @DisplayName("Debería retornar 400 cuando los datos del cliente no son válidos")
    public void testCreateClientInvalid() {
        Client invalidClient = new Client(
                "John Doe", "InvalidoGenero", 30, "12345", "123 Main St", "123",
                null, "password123", true
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl, invalidClient, Map.class);

        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Error de validación", response.getBody().get("message"));

        List<Map<String, String>> errors = (List<Map<String, String>>) response.getBody().get("errors");
        assertTrue(errors.stream().anyMatch(error -> error.get("field").equals("telefono")));
    }
}
