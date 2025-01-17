package com.empresa.msaccountmovements.infrastructure.controllers;

import com.empresa.msaccountmovements.application.AccountMovementApplicationService;
import com.empresa.msaccountmovements.domain.dtos.AccountRequestDto;
import com.empresa.msaccountmovements.domain.dtos.AccountResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class AccountController {

    private final AccountMovementApplicationService accountService;


    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("{accountId}")
    public ResponseEntity<AccountResponseDto> getOneAccount(@PathVariable String accountId) {
        return ResponseEntity.ok(accountService.findOne(accountId));
    }

    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody AccountRequestDto requestDTO) {
        return ResponseEntity.ok(accountService.createAccount(requestDTO));
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> update(@PathVariable String accountId, @Valid @RequestBody AccountRequestDto requestDTO) {
        return ResponseEntity.ok(accountService.update(accountId, requestDTO));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}
