package com.br.javapay.infra.exception;

public class SaldoInsulficienteOuInválidoException extends RuntimeException {
    public SaldoInsulficienteOuInválidoException(String message) {
        super(message);
    }
}
