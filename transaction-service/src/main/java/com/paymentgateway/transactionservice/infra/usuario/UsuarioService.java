package com.paymentgateway.transactionservice.infra.usuario;

import com.paymentgateway.transactionservice.infra.exception.EmailJaCadastradoException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Usuario cadastrar(CadastroUsuarioDTO cadastroUsuarioDTO) {
        if (usuarioRepository.findByEmail(cadastroUsuarioDTO.getEmail()) != null) {
            throw new EmailJaCadastradoException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(cadastroUsuarioDTO.getUsername());
        usuario.setEmail(cadastroUsuarioDTO.getEmail());
        usuario.setPassword(cadastroUsuarioDTO.getPassword());

        return usuarioRepository.save(usuario);
    }

    public Usuario atualizarUsuario(Long id, CadastroUsuarioDTO cadastroUsuarioDTO) {
        Usuario usuario = buscarUsuarioPorId(id);
        usuario.setUsername(cadastroUsuarioDTO.getUsername());
        usuario.setEmail(cadastroUsuarioDTO.getEmail());
        usuario.setPassword(cadastroUsuarioDTO.getPassword());
        return usuarioRepository.save(usuario);
    }

    public Usuario deleteUsuario(Long id) {
        Usuario usuario = buscarUsuarioPorId(id);
        usuarioRepository.delete(usuario);
        return usuario;
    }
}
