package com.br.javapay.domain.tranferencia;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferenciaResponseDTO(
        Long id,
        BigDecimal valorTransferido,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime dataTransferencia,

        StatusTransferencia status,
        BigDecimal saldoRestanteOrigem,
        Long idContaDestino
) {}