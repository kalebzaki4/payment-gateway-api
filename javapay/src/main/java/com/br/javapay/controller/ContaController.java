package com.br.javapay.controller;

import com.br.javapay.domain.conta.Conta;
import com.br.javapay.domain.conta.ContaService;
import com.br.javapay.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contas")
public class ContaController {
    private final ContaService contaService;

    @Autowired
    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    // ver saldo da conta
    @GetMapping(value = "/saldo")
    public ResponseEntity<Conta> verSaldo(@AuthenticationPrincipal Usuario usuario) {
        Conta saldoDaConta = contaService.verSaldoDaConta(usuario);
        return ResponseEntity.ok(saldoDaConta);
    }
}
