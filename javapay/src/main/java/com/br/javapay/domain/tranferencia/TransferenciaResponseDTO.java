package com.br.javapay.domain.tranferencia;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferenciaResponseDTO(
        Long id,
        BigDecimal valorTransferido,
        LocalDateTime dataTransferencia,
        StatusTransferencia status,
        BigDecimal saldoRestanteOrigem,
        Long idContaDestino
) {}