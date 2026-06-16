package com.paymentgateway.transactionservice.infra.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CadastroUsuarioDTO(@NotBlank String nome, @NotBlank @Email String email, @NotBlank String senha) {
}
