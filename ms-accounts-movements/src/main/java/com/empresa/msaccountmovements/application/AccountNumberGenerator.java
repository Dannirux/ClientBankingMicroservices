package com.empresa.msaccountmovements.application;

import com.empresa.msaccountmovements.domain.models.enums.AccountType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AccountNumberGenerator {
    public String generate(AccountType accountType) {
        String prefix = switch (accountType.toString()) {
            case "AHORRO" -> "01";
            case "CORRIENTE" -> "02";
            default -> "00";
        };
        String randomId = String.format("%06d", new Random().nextInt(999999));
        int checksum = calculateChecksum(prefix + randomId);
        return prefix + randomId + checksum;
    }

    private int calculateChecksum(String input) {
        return input.chars().map(Character::getNumericValue).sum() % 10;
    }
}
