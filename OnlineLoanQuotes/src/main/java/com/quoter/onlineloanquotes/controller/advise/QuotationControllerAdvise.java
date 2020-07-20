package com.quoter.onlineloanquotes.controller.advise;

import com.quoter.onlineloanquotes.controller.error.ErrorMessage;
import com.quoter.onlineloanquotes.exceptions.AmountException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class QuotationControllerAdvise {

    @Value("${onlineloanquotes.api.version}")
    private String apiVersion;

    @ExceptionHandler(AmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleAmountException(AmountException e) {
        return new ErrorMessage(apiVersion, HttpStatus.BAD_REQUEST.value(), e.getMessage(), AmountException.class.getSimpleName());
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public String handleWrongMediaType() {
        return "Acceptable MIME type:" + MediaType.APPLICATION_JSON_VALUE;
    }
}