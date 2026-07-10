package com.br.javapay.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ContaNaoEncontradaException.class)
    public ResponseEntity<ErrorMessageDTO> handleContaNaoEncontradaException(ContaNaoEncontradaException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErrorMessageDTO> handleUsuarioNaoEncontradoException(UsuarioNaoEncontradoException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ContaOrigemInativaException.class)
    public ResponseEntity<ErrorMessageDTO> handleContaOrigemInativaException(ContaOrigemInativaException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ContaDestinoInativaException.class)
    public ResponseEntity<ErrorMessageDTO>  handleContaDestinoInativaException(ContaDestinoInativaException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SaldoInsulficienteException.class)
    public ResponseEntity<ErrorMessageDTO>  handleSaldoInsulficienteException(SaldoInsulficienteException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CpfInvalidoException.class)
    public ResponseEntity<ErrorMessageDTO>  handleCpfInvalidoException(CpfInvalidoException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorMessageDTO> buildResponse(String message, HttpStatus status) {
        ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO(message, LocalDateTime.now(ZoneId.of("UTC")).toString());
        return ResponseEntity.status(status).body(errorMessageDTO);
    }
}
