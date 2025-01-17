package com.empresa.msaccountmovements.unit.controllers;

import com.empresa.msaccountmovements.application.AccountMovementApplicationService;
import com.empresa.msaccountmovements.domain.dtos.AccountRequestDto;
import com.empresa.msaccountmovements.domain.dtos.AccountResponseDto;
import com.empresa.msaccountmovements.infrastructure.controllers.AccountController;
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

@DisplayName("Pruebas para AccountController")
public class AccountControllerUnitTest {

    @Mock
    private AccountMovementApplicationService accountService;

    @InjectMocks
    private AccountController accountController;

    private AccountResponseDto validAccount;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validAccount = new AccountResponseDto("1", "1234567890", null, 1000.0, true, null);
    }

    @Nested
    @DisplayName("Pruebas para el endpoint GET /cuentas/{accountId}")
    class GetAccountByIdTests {

        @Test
        @DisplayName("Debería retornar una cuenta existente")
        public void testGetAccountById_Success() {
            when(accountService.findOne("1")).thenReturn(validAccount);

            ResponseEntity<AccountResponseDto> response = accountController.getOneAccount("1");

            assertNotNull(response);
            assertEquals(200, response.getStatusCodeValue());
            assertEquals("1234567890", response.getBody().getNumeroCuenta());
        }

        @Test
        @DisplayName("Debería lanzar una excepción si la cuenta no existe")
        public void testGetAccountById_NotFound() {
            when(accountService.findOne("999")).thenThrow(new IllegalArgumentException("Cuenta no encontrada"));

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                accountController.getOneAccount("999");
            });

            assertEquals("Cuenta no encontrada", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Pruebas para el endpoint POST /cuentas")
    class CreateAccountTests {

        @Test
        @DisplayName("Debería crear una cuenta exitosamente")
        public void testCreateAccount_Success() {
            AccountRequestDto requestDto = new AccountRequestDto(null, 1000.0, true, "1");
            when(accountService.createAccount(requestDto)).thenReturn(validAccount);

            ResponseEntity<AccountResponseDto> response = accountController.createAccount(requestDto);

            assertNotNull(response);
            assertEquals(200, response.getStatusCodeValue());
            assertEquals("1234567890", response.getBody().getNumeroCuenta());
        }

        @Test
        @DisplayName("Debería lanzar una excepción al crear una cuenta con datos inválidos")
        public void testCreateAccount_InvalidData() {
            AccountRequestDto invalidRequest = new AccountRequestDto(null, -1000.0, true, "1");

            when(accountService.createAccount(invalidRequest))
                    .thenThrow(new IllegalArgumentException("Saldo inicial no puede ser negativo"));

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                accountController.createAccount(invalidRequest);
            });

            assertEquals("Saldo inicial no puede ser negativo", exception.getMessage());
        }
    }
}