package com.br.javapay.domain.tranferencia;

import java.math.BigDecimal;

public record TransferenciaRequestDTO(
        Long contaInicial,
        Long contaFinal,
        BigDecimal saldo) {

}
