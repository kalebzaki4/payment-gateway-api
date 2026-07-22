package com.br.javapay.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateDTO(
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
        String nome,

        @Email(message = "Formato de e-mail inválido.")
        String email,

        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String senha
) {}