package com.apulbere.cqrs;

import lombok.Value;

@Value
public class ValidationResult {
    private static final ValidationResult VALID = new ValidationResult(null, true);

    private String message;
    private boolean isValid;

    public static ValidationResult valid() {
        return VALID;
    }
}
