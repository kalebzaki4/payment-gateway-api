package com.br.javapay.infra.exception;

import java.util.List;

public record ErrorMessageDTO(
        String message,
        String details,
        List<ValidationErrorDTO> errors) {
}
