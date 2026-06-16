package com.paymentgateway.transactionservice.infra.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CadastroUsuarioDTO(@NotBlank String username, @NotBlank @Email String email, @NotBlank String password) {
    public String getEmail() {
        return email;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
