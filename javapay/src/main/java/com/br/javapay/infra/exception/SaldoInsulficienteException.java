package com.br.javapay.infra.exception;

public class SaldoInsulficienteException extends RuntimeException {
    public SaldoInsulficienteException(String message) {
        super(message);
    }
}
