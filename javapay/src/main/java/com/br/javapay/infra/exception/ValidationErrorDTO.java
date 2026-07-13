package com.br.javapay.infra.exception;

public record ValidationErrorDTO(
        String campoErro,
        String mensagemErro
) {
}
