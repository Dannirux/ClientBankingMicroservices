package com.empresa.msaccountmovements.infrastructure.controllers;

import com.empresa.msaccountmovements.ReportApplicationService;
import com.empresa.msaccountmovements.domain.dtos.MovementListDto;
import com.empresa.msaccountmovements.domain.dtos.ReportResponseDto;
import com.empresa.msaccountmovements.domain.services.MovementDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReportController {

    private final MovementDomainService movementService;
    private final ReportApplicationService reportService;

    @GetMapping
    public ResponseEntity<List<MovementListDto>> getReport(
            @RequestParam("fecha") String fechaRango,
            @RequestParam(value = "cliente", required = false) String clientId) {

        Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})\\s*to\\s*(\\d{4}-\\d{2}-\\d{2})");
        Matcher matcher = pattern.matcher(fechaRango);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("El formato de la fecha debe ser 'YYYY-MM-DD to YYYY-MM-DD'");
        }

        LocalDate startDate = LocalDate.parse(matcher.group(1), DateTimeFormatter.ISO_DATE);
        LocalDate endDate = LocalDate.parse(matcher.group(2), DateTimeFormatter.ISO_DATE);

        if (clientId != null) {
            return ResponseEntity.ok(movementService.listMovementsByClientAndDateRange(clientId, startDate, endDate));
        } else {
            return ResponseEntity.ok(movementService.listMovementsByDateRange(startDate, endDate));
        }
    }

    @GetMapping("estado-de-cuenta")
    public ResponseEntity<ReportResponseDto> getStateAccount(
            @RequestParam("fecha") String fechaRango,
            @RequestParam(value = "cliente", required = true) String clientId) {

        Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})\\s*to\\s*(\\d{4}-\\d{2}-\\d{2})");
        Matcher matcher = pattern.matcher(fechaRango);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("El formato de la fecha debe ser 'YYYY-MM-DD to YYYY-MM-DD'");
        }

        LocalDate startDate = LocalDate.parse(matcher.group(1), DateTimeFormatter.ISO_DATE);
        LocalDate endDate = LocalDate.parse(matcher.group(2), DateTimeFormatter.ISO_DATE);

        return ResponseEntity.ok(reportService.generateAccountStatement(clientId, startDate, endDate));
    }
}
