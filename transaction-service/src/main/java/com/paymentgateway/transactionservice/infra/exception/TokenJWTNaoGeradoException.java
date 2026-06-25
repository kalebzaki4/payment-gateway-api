package com.paymentgateway.transactionservice.infra.exception;

import com.auth0.jwt.exceptions.JWTCreationException;

public class TokenJWTNaoGeradoException extends RuntimeException {
    public TokenJWTNaoGeradoException(String message, JWTCreationException exception) {
        super(message);
    }
}
