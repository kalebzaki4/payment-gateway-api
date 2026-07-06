package com.br.javapay.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.br.javapay.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {
    @Value("${security.password.encoder.secret}")
    private String secretKey;

    public String generateToken(Usuario usuario) {
        var algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withIssuer("JavaPay")
                .withClaim("id", usuario.getId())
                .withExpiresAt(Expiration())
                .withSubject(usuario.getEmail())
                .sign(algorithm);
    }

    public String getSubject(String token) {
        var algorithm = Algorithm.HMAC256(secretKey);
        return JWT.require(algorithm)
                .withIssuer("JavaPay")
                .build()
                .verify(token)
                .getSubject();
    }

    private Instant Expiration() {
        return Instant.now().plusSeconds(2000);
    }
}
