package com.paymentgateway.transactionservice.infra.exception;

import com.paymentgateway.transactionservice.domain.usuario.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailJaCadastradoException.class)
    public String handleEmailJaCadastradoException(EmailJaCadastradoException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT).getBody().getMensagem();
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public String handleUsuarioNaoEncontradoException(UsuarioNaoEncontradoException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND).getBody().getMensagem();
    }

    @ExceptionHandler(TokenJWTNaoGeradoException.class)
    public ResponseEntity<ErroResponse> handleTokenJWTNaoGeradoException(TokenJWTNaoGeradoException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErroResponse> buildResponse(String message, HttpStatus status) {
        ErroResponse erro = new ErroResponse(message, LocalDateTime.now());
        return ResponseEntity.status(status).body(erro);
    }
}
