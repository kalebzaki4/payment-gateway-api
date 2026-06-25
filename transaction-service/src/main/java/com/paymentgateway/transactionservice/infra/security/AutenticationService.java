package com.paymentgateway.transactionservice.infra.security;

import com.paymentgateway.transactionservice.domain.usuario.UsuarioRepository;
import com.paymentgateway.transactionservice.infra.exception.UsuarioNaoEncontradoException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticationService implements UserDetailsService {

    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

    }
}
