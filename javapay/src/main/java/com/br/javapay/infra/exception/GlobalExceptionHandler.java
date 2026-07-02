package com.br.javapay.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDTO> handleUsuarioNaoEncontradoException(UsuarioNaoEncontradoException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ErroResponseDTO> buildResponse(String message, HttpStatus status) {
        ErroResponseDTO erro = new ErroResponseDTO(message, LocalDateTime.now(ZoneId.of("UTC")).toString());
        return ResponseEntity.status(status).body(erro);
    }
}
