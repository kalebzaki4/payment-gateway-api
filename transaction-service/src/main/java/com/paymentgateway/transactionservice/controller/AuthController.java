package com.paymentgateway.transactionservice.controller;

import com.paymentgateway.transactionservice.domain.usuario.DadosAutenticacaoDTO;
import com.paymentgateway.transactionservice.infra.security.AutenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final AutenticationService  autenticationService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService, AutenticationService autenticationService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.autenticationService = autenticationService;
    }

    @PostMapping
    public ResponseEntity<String> login(DadosAutenticacaoDTO dadosAutenticacaoDTO) {
        var autenticationToken = new UsernamePasswordAuthenticationToken(dadosAutenticacaoDTO.email(), dadosAutenticacaoDTO.password());
        var authentication = authenticationManager.authenticate(autenticationToken);
        String token = tokenService.gerarToken(authentication);
        return ResponseEntity.ok(token);
    }
}
