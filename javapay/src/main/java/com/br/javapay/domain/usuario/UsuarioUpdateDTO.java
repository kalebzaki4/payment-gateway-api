package com.br.javapay.domain.usuario;

public record UsuarioUpdateDTO(
        String nome,
        String email,
        String senha
) {
}
