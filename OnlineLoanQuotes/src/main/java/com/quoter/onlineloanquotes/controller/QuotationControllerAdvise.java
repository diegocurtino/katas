package com.quoter.onlineloanquotes.controller;

import com.quoter.onlineloanquotes.exception.AmountException;
import com.quoter.onlineloanquotes.exception.QuoteException;
import com.quoter.onlineloanquotes.exception.SourceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;

@RestControllerAdvice
public class QuotationControllerAdvise {
    private static final Logger LOGGER = LogManager.getLogger(QuotationControllerAdvise.class);

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

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleIoException() {
        LOGGER.info("An I/O exception occurred while producing a quote");
        return new ErrorMessage(apiVersion, HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while processing a quote. Try again later", "Internal error");
    }

    @ExceptionHandler(QuoteException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleQuoteException(QuoteException e) {
        return new ErrorMessage(apiVersion, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), QuoteException.class.getSimpleName());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMissingParamException(MissingServletRequestParameterException e) {
        LOGGER.info(e.getMessage());
        return new ErrorMessage(apiVersion, HttpStatus.BAD_REQUEST.value(), e.getMessage(), MissingServletRequestParameterException.class.getSimpleName());
    }

    @ExceptionHandler(SourceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleSourceException(SourceException e) {
        LOGGER.info(e.getMessage());
        return new ErrorMessage(apiVersion, HttpStatus.BAD_REQUEST.value(), e.getMessage(), SourceException.class.getSimpleName());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        LOGGER.info(e.getMessage());
        return new ErrorMessage(apiVersion, HttpStatus.BAD_REQUEST.value(), e.getMessage(), MethodArgumentTypeMismatchException.class.getSimpleName());
    }
}