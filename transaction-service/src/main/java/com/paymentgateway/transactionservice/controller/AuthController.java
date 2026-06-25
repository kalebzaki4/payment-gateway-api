package com.paymentgateway.transactionservice.controller;

import com.paymentgateway.transactionservice.domain.usuario.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.paymentgateway.transactionservice.infra.security.TokenService;

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

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody CadastroUsuarioDTO cadastroUsuarioDTO) {
        usuarioService.cadastrar(cadastroUsuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<DadosTokenJWT> login(@RequestBody @Valid DadosAutenticacaoDTO dadosAutenticacaoDTO) {
        var autenticationToken = new UsernamePasswordAuthenticationToken(dadosAutenticacaoDTO.email(), dadosAutenticacaoDTO.password());
        var authentication = authenticationManager.authenticate(autenticationToken);
        String token = tokenService.generateToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new DadosTokenJWT(token));
    }

}
