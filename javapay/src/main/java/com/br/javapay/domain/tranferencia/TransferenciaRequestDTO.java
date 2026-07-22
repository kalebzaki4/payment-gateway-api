package com.br.javapay.domain.tranferencia;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferenciaRequestDTO(Long contaInicial, Long contaFinal,

                                      @NotNull(message = "O valor é obrigatório.") @Positive(message = "O valor deve ser maior que zero.") BigDecimal saldo,

                                      String cpf) {
}
