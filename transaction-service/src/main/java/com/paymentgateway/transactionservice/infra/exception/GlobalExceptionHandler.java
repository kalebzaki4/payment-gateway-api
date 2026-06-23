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
        return ex.getMessage();
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public String handleUsuarioNaoEncontradoException(UsuarioNaoEncontradoException ex) {
        return ex.getMessage();
    }

    private ResponseEntity<ErroResponse> buildResponse(String message, HttpStatus status) {
        ErroResponse erro = new ErroResponse(message, LocalDateTime.now());
        return ResponseEntity.status(status).body(erro);
    }
}
