package com.paymentgateway.transactionservice.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.paymentgateway.transactionservice.domain.usuario.Usuario;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {
    private String secretKey = "mySecretKey";

    public String generateToken(Usuario usuario) {
        var algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withIssuer("payment-gateway")
                .withSubject(usuario.getEmail())
                .withClaim("id", usuario.getId())
                .withExpiresAt(expirationDate())
                .sign(algorithm);
    }

    public String getSubject(String token) {
        var algorithm = Algorithm.HMAC256(secretKey);
        return JWT.require(algorithm)
                .withIssuer("payment-gateway")
                .build()
                .verify(token)
                .getSubject();
    }

    private Instant expirationDate() {
        return Instant.now().plusSeconds(3600);
    }
}
