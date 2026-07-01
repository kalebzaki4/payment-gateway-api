package com.br.javapay.domain.usuario;

import com.br.javapay.domain.conta.Conta;
import com.br.javapay.domain.conta.Status;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
}
