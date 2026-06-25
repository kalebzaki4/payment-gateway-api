package com.paymentgateway.transactionservice.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosAutenticacaoDTO(@NotBlank @Email String email, @NotBlank String password) {

}
