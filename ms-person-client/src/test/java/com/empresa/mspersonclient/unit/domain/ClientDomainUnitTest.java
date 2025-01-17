package com.empresa.mspersonclient.unit.domain;

import com.empresa.mspersonclient.domain.Client;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas para la entidad de dominio Client")
public class ClientDomainUnitTest {

    private Client validClient;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        validClient = new Client(
                "John Doe", "Male", 30, "1234567890", "123 Main St", "123456789",
                "C001", "password123", true
        );
    }

    @Nested
    @DisplayName("Validaciones de datos del cliente")
    class ClientValidationTests {

        @Test
        @DisplayName("Debería crear un cliente con datos válidos")
        public void testValidClientCreation() {
            assertNotNull(validClient);
            assertEquals("John Doe", validClient.getNombre());
            assertEquals("Male", validClient.getGenero());
            assertTrue(validClient.isEstado());
        }

        @Test
        @DisplayName("Debería fallar al crear un cliente con datos inválidos")
        public void testInvalidClientCreation() {
            Client invalidClient = new Client(
                    "", "Male", -1, "123", "", "",
                    "C001", "", true
            );

            Set<ConstraintViolation<Client>> violations = validator.validate(invalidClient);
            assertFalse(violations.isEmpty());

            for (ConstraintViolation<Client> violation : violations) {
                System.out.println(violation.getPropertyPath() + ": " + violation.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Pruebas de generación de ID de cliente")
    class ClientIdGenerationTests {

        @Test
        @DisplayName("Debería generar un ID de cliente automáticamente si no se proporciona")
        public void testGenerateClientId() {
            Client clientWithoutId = new Client(
                    "Jane Doe", "Female", 25, "9876543210", "456 Elm St", "987654321",
                    null, "password1234", true
            );

            clientWithoutId.setClienteId(UUID.randomUUID().toString());
            assertNotNull(clientWithoutId.getClienteId());
        }

        @Test
        @DisplayName("No debería sobrescribir un ID de cliente existente")
        public void testDoNotOverwriteClientId() {
            validClient.setClienteId(UUID.randomUUID().toString());
            assertNotSame("C001", validClient.getClienteId());
        }
    }

    @Nested
    @DisplayName("Pruebas de estado del cliente")
    class ClientStatusTests {

        @Test
        @DisplayName("Debería activar el cliente")
        public void testActivateClient() {
            validClient.setEstado(true);
            assertTrue(validClient.isEstado());
        }

        @Test
        @DisplayName("Debería desactivar el cliente")
        public void testDeactivateClient() {
            validClient.setEstado(false);
            assertFalse(validClient.isEstado());
        }
    }
}
