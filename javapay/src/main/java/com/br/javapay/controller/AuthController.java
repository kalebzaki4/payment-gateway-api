package com.br.javapay.controller;

import com.br.javapay.domain.usuario.Usuario;
import com.br.javapay.domain.usuario.UsuarioRequestDTO;
import com.br.javapay.domain.usuario.UsuarioService;
import com.br.javapay.infra.security.DadosTokenJwtDto;
import com.br.javapay.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final UsuarioService usuarioService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        var usuarioLogin = new UsernamePasswordAuthenticationToken(usuarioRequestDTO.email(), usuarioRequestDTO.senha());
        var authentication = authenticationManager.authenticate(usuarioLogin);
        var token = tokenService.generateToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJwtDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UsuarioRequestDTO usuarioRequestDTO, UriComponentsBuilder uriComponentsBuilder) {
        var usuario = usuarioService.createUsuario(usuarioRequestDTO);
        var uri = uriComponentsBuilder.path("/auth/register/{id}").buildAndExpand(usuario.getId());
        return ResponseEntity.created(uri.toUri()).body(usuario);
    }
}
