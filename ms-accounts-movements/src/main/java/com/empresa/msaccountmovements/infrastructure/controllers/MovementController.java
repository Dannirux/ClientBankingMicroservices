package com.empresa.msaccountmovements.infrastructure.controllers;

import com.empresa.msaccountmovements.domain.dtos.MovementListDto;
import com.empresa.msaccountmovements.domain.dtos.MovementRequestDto;
import com.empresa.msaccountmovements.domain.models.Movement;
import com.empresa.msaccountmovements.domain.services.MovementDomainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovementController {

    private final MovementDomainService movementService;

    @PostMapping
    public ResponseEntity<Movement> registerMovement(@Valid @RequestBody MovementRequestDto movement) {
        return ResponseEntity.ok(movementService.registerMovement(movement));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovement(@PathVariable String id) {
        movementService.deleteMovement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cuenta/{accountId}")
    public ResponseEntity<List<Movement>> getMovementsByAccount(@PathVariable String accountId) {
        return ResponseEntity.ok(movementService.getMovementsByAccount(accountId));
    }
}
