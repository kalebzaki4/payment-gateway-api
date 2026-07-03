package com.br.javapay.domain.usuario;

public record UsuarioRequestDTO(
        String cpf,
        String email,
        String senha
) {
}
