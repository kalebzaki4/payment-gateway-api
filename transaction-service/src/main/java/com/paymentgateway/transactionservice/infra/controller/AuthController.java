package com.paymentgateway.transactionservice.infra.controller;

import com.paymentgateway.transactionservice.infra.security.AutenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AutenticationService autenticationService;

    public AuthController(AutenticationService autenticationService) {
        this.autenticationService = autenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity
}
