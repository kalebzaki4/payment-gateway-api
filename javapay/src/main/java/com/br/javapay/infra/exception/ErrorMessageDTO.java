package com.br.javapay.infra.exception;

public record ErrorMessageDTO(
        String message,
        String details) {
}
