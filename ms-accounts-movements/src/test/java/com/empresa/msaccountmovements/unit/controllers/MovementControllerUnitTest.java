package com.empresa.msaccountmovements.unit.controllers;

import com.empresa.msaccountmovements.domain.dtos.MovementRequestDto;
import com.empresa.msaccountmovements.domain.models.Movement;
import com.empresa.msaccountmovements.domain.services.MovementDomainService;
import com.empresa.msaccountmovements.infrastructure.controllers.MovementController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Pruebas para MovementController")
public class MovementControllerUnitTest {

    @Mock
    private MovementDomainService movementService;

    @InjectMocks
    private MovementController movementController;

    private Movement validMovement;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validMovement = new Movement();
        validMovement.setId("1");
        validMovement.setValor(500.0);
    }

    @Nested
    @DisplayName("Pruebas para el endpoint POST /movimientos")
    class RegisterMovementTests {

        @Test
        @DisplayName("Debería registrar un movimiento exitosamente")
        public void testRegisterMovement_Success() {
            MovementRequestDto requestDto = new MovementRequestDto("DEPOSITO", "1234567890", 500.0);
            when(movementService.registerMovement(requestDto)).thenReturn(validMovement);

            ResponseEntity<Movement> response = movementController.registerMovement(requestDto);

            assertNotNull(response);
            assertEquals(200, response.getStatusCodeValue());
            assertEquals(500.0, response.getBody().getValor());
        }

        @Test
        @DisplayName("Debería lanzar una excepción al registrar un movimiento con datos inválidos")
        public void testRegisterMovement_InvalidData() {
            MovementRequestDto invalidRequest = new MovementRequestDto("RETIRO", "1234567890", -100.0);

            when(movementService.registerMovement(invalidRequest))
                    .thenThrow(new IllegalArgumentException("El valor del movimiento no puede ser negativo"));

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                movementController.registerMovement(invalidRequest);
            });

            assertEquals("El valor del movimiento no puede ser negativo", exception.getMessage());
        }
    }
}