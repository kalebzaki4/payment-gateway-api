package com.paymentgateway.transactionservice.infra.usuario;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final userRepository userRepository;

    public UsuarioService(userRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Usuario> listarUsuarios() {
        return userRepository.findAll();
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Usuario cadastrar(CadastroUsuarioDTO cadastroUsuarioDTO) {

    }
}
