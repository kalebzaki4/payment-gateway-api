package com.br.javapay.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<ErrorMessageDTO> handleContaDestinoInativaException(ContaDestinoInativaException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SaldoInsulficienteException.class)
    public ResponseEntity<ErrorMessageDTO> handleSaldoInsulficienteException(SaldoInsulficienteException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CpfInvalidoException.class)
    public ResponseEntity<ErrorMessageDTO> handleCpfInvalidoException(CpfInvalidoException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorMessageDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessageDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ValidationErrorDTO> erros = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            erros.add(new ValidationErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return buildResponse("Dados inválidos", HttpStatus.BAD_REQUEST, erros);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMessageDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorMessageDTO> buildResponse(String message, HttpStatus status) {
        ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO(message, LocalDateTime.now(ZoneId.of("UTC")).toString(), null);
        return ResponseEntity.status(status).body(errorMessageDTO);
    }

    private ResponseEntity<ErrorMessageDTO> buildResponse(String message, HttpStatus status, List<ValidationErrorDTO> errors) {
        ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO(message, LocalDateTime.now(ZoneId.of("UTC")).toString(), errors);
        return ResponseEntity.status(status).body(errorMessageDTO);
    }
}
