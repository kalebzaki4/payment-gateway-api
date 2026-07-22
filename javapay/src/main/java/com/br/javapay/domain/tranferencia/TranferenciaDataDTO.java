package com.br.javapay.domain.tranferencia;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record TranferenciaDataDTO(
        @NotNull(message = "A data da transferência é obrigatória.")
        @PastOrPresent(message = "A data da transferência não pode ser uma data futura.")
        LocalDateTime dataTransferencia ) {

}
