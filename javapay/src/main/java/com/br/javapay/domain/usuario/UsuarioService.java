package com.br.javapay.domain.usuario;

import com.br.javapay.domain.conta.Conta;
import com.br.javapay.domain.conta.Status;
import com.br.javapay.infra.exception.UsuarioNaoEncontradoException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    // ver todos os usuários
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    // ver um usuário específico
    public Usuario findById(Long id) {
        if (usuarioRepository.findById(id).isPresent()) {
            return usuarioRepository.findById(id).get();
        }
        throw new UsuarioNaoEncontradoException("Usuário não encontrado com o ID: " + id);
    }

    // criar usuário
    public Usuario createUsuario(UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioRequestDTO, usuario);
        usuario.setPassword(passwordEncoder.encode(usuarioRequestDTO.password()));
        usuario.setRole(Roles.USER);
        Conta conta = new Conta();
        conta.setValor(BigDecimal.ZERO);
        conta.setStatusConta(Status.ATIVO);
        usuario.setConta(conta);
        return usuarioRepository.save(usuario);
    }

    // atualizar usuário
    public Usuario updateUsuario(Long id, UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usuarioExistente = findById(id);
        if (usuarioRequestDTO.email() != null && !usuarioRequestDTO.email().isBlank()) {
            usuarioExistente.setEmail(usuarioRequestDTO.email());
        }
        if (usuarioRequestDTO.password() != null && !usuarioRequestDTO.password().isBlank()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioRequestDTO.password()));
        }
        return usuarioRepository.save(usuarioExistente);
    }

}
