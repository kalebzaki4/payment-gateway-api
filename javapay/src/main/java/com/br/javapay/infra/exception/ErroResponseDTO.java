package com.br.javapay.infra.exception;

public record ErroResponseDTO(
        String message,
        String timestamp) {
}
