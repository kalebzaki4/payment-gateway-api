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

    public Usuario findById(long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario createUsuario(UsuarioRequestDTO usuarioRequestDTO) {
        String cpfLimpo = usuarioRequestDTO.cpf().replaceAll("[^0-9]", "");
        if (!validateCpf(cpfLimpo)) {
            throw new RuntimeException("CPF inválido");
        }
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioRequestDTO, usuario);
        usuario.setCpf(cpfLimpo);
        usuario.setSenha(encoder.encode(usuario.getSenha()));
        usuario.setRole(Roles.USER);
        Conta conta = new Conta();
        conta.setValor(BigDecimal.ZERO);
        conta.setStatus(Status.ATIVO);
        usuario.setConta(conta);

        return usuarioRepository.save(usuario);
    }

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

    public void deleteUsuario(long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.getConta().setStatus(Status.INATIVO);
        usuarioRepository.save(usuario);
    }

    private boolean validateCpf(String cpf) {
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int resto = 11 - (soma % 11);
        if (resto == 10 || resto == 11) resto = 0;
        if (resto != Character.getNumericValue(cpf.charAt(9))) return false;

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        resto = 11 - (soma % 11);
        if (resto == 10 || resto == 11) resto = 0;
        if (resto != Character.getNumericValue(cpf.charAt(10))) return false;

        return true;
    }
}