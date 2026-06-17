package com.paymentgateway.transactionservice.infra.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailJaCadastradoException.class)
    public String handleEmailJaCadastradoException(EmailJaCadastradoException ex) {
        return ex.getMessage();
    }
}
