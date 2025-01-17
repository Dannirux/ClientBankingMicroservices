package com.empresa.msaccountmovements.unit.controllers;

import com.empresa.msaccountmovements.ReportApplicationService;
import com.empresa.msaccountmovements.domain.dtos.ReportResponseDto;
import com.empresa.msaccountmovements.infrastructure.controllers.ReportController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Pruebas para ReportController")
public class ReportControllerUnitTest {

    @Mock
    private ReportApplicationService reportService;

    @InjectMocks
    private ReportController reportController;

    private ReportResponseDto validReport;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validReport = new ReportResponseDto("1", null);
    }

    @Nested
    @DisplayName("Pruebas para estado de cuenta")
    class GetAccountStateTests {

        @Test
        @DisplayName("Debería retornar el estado de cuenta correctamente")
        public void testGetAccountState_Success() {
            when(reportService.generateAccountStatement(eq("1"), any(), any())).thenReturn(validReport);

            ReportResponseDto response = reportController.getStateAccount("2023-01-01 to 2023-01-31", "1").getBody();

            assertNotNull(response);
            assertEquals("1", response.getClienteId());
        }

        @Test
        @DisplayName("Debería lanzar una excepción si el rango de fechas es inválido")
        public void testGetAccountState_InvalidDateRange() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                reportController.getStateAccount("invalid-date-range", "1");
            });

            assertEquals("El formato de la fecha debe ser 'YYYY-MM-DD to YYYY-MM-DD'", exception.getMessage());
        }
    }
}
