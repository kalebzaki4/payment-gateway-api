package com.paymentgateway.transactionservice.domain.usuario;

import java.time.LocalDateTime;

public record ErroResponse(String message, LocalDateTime timestamp) {
}
