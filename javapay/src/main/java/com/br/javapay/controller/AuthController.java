package com.br.javapay.controller;

import com.br.javapay.domain.usuario.Usuario;
import com.br.javapay.domain.usuario.UsuarioRequestDTO;
import com.br.javapay.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<String> authenticate(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        var credencial = new UsernamePasswordAuthenticationToken(usuarioRequestDTO.email(), usuarioRequestDTO.password());
        Authentication authentication = authenticationManager.authenticate(credencial);
        authentication.getPrincipal();
        String token = tokenService.generateToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(token);
    }
}
