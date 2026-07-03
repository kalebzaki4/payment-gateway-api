package com.br.javapay.domain.usuario;

import com.br.javapay.infra.exception.UsuarioNaoEncontradoException;
import com.br.javapay.domain.conta.Conta;
import com.br.javapay.domain.conta.Status;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder encoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder encoder) {
        this.usuarioRepository = usuarioRepository;
        this.encoder = encoder;
    }

    // procurar usuario por ID
    public Usuario findById(long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
    }

    // procurar todos os usuarios
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    // criar usuario
    public Usuario createUsuario(UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioRequestDTO, usuario);
        usuario.setSenha(encoder.encode(usuario.getSenha()));
        usuario.setRole(Roles.USER);
        Conta conta = new Conta();
        conta.setSaldo(BigDecimal.ZERO);
        conta.setStatus(Status.ATIVO);
        usuario.setConta(conta);
        return usuarioRepository.save(usuario);
    }

    // atualizar usuario
    public Usuario updateUsuario(long id, UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usuario = findById(id);
        if (usuarioRequestDTO.email() != null && !usuarioRequestDTO.email().isBlank()) {
            usuario.setEmail(usuarioRequestDTO.email());
        }
        if (usuarioRequestDTO.senha() != null && !usuarioRequestDTO.senha().isBlank()) {
            usuario.setSenha(encoder.encode(usuarioRequestDTO.senha()));
        }
        return usuarioRepository.save(usuario);
    }

    // deletar usuário
    public void deleteUsuario(long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.getConta().setStatus(Status.INATIVO);

        usuarioRepository.save(usuario);
    }
}
