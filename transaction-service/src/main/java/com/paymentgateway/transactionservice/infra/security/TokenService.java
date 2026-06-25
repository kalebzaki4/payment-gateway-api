package com.paymentgateway.transactionservice.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.paymentgateway.transactionservice.domain.usuario.Usuario;
import com.paymentgateway.transactionservice.infra.exception.TokenJWTNaoGeradoException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {
    private String tokenSecret = "eyJhbGciOi";

    public String generateToken(Usuario usuario) {
        try {
            var algorithm = Algorithm.HMAC256(tokenSecret);
            return JWT.create()
                    .withIssuer("API Payment Gateway")
                    .withExpiresAt(getExpirationDate())
                    .withSubject(usuario.getEmail())
                    .withClaim("id", usuario.getId())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new TokenJWTNaoGeradoException("Erro ao gerar token", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        var algorithm = Algorithm.HMAC256(tokenSecret);
        return JWT.require(algorithm)
                .withIssuer("API Payment Gateway")
                .build()
                .verify(tokenJWT)
                .getSubject();
    }

    private Instant getExpirationDate() {
        return Instant.now().plusSeconds(3600);
    }

}
