package com.paymentgateway.transactionservice.domain.usuario;

import com.paymentgateway.transactionservice.infra.exception.EmailJaCadastradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
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
        usuario.setPassword(passwordEncoder.encode(cadastroUsuarioDTO.getPassword()));

        return usuarioRepository.save(usuario);
    }

    public Usuario atualizarUsuario(Long id, CadastroUsuarioDTO cadastroUsuarioDTO) {
        Usuario usuario = buscarUsuarioPorId(id);
        usuario.setUsername(cadastroUsuarioDTO.getUsername());
        usuario.setEmail(cadastroUsuarioDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(cadastroUsuarioDTO.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public Usuario deleteUsuario(Long id) {
        Usuario usuario = buscarUsuarioPorId(id);
        usuarioRepository.delete(usuario);
        return usuario;
    }
}
