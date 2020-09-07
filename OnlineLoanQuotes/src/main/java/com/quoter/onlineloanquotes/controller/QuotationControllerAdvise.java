package com.quoter.onlineloanquotes.controller;

import com.quoter.onlineloanquotes.exceptions.AmountException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.net.URISyntaxException;

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

    @ExceptionHandler({IOException.class, URISyntaxException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleIoException() {
        return new ErrorMessage(apiVersion, HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while processing a quote. Try again later", "Internal error");
    }
}