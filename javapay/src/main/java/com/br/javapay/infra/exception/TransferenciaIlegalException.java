package com.br.javapay.infra.exception;

public class TransferenciaIlegalException extends RuntimeException {
    public TransferenciaIlegalException(String message) {
        super(message);
    }
}
