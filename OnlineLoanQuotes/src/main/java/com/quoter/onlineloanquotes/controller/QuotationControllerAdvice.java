package com.quoter.onlineloanquotes.controller;

import com.quoter.onlineloanquotes.exception.AmountException;
import com.quoter.onlineloanquotes.exception.QuoteException;
import com.quoter.onlineloanquotes.exception.SourceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.MissingRequestValueException;

import java.io.IOException;

@RestControllerAdvice
public class QuotationControllerAdvice {
    private static final Logger LOGGER = LogManager.getLogger(QuotationControllerAdvice.class);

    @Value("${onlineloanquotes.api.version}")
    private String apiVersion;

    @ExceptionHandler(AmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleAmountException(AmountException e) {
        return new ErrorMessage(apiVersion, HttpStatus.BAD_REQUEST.value(), e.getMessage(), AmountException.class.getSimpleName());
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

    @ExceptionHandler(MissingRequestValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMissingParamException(MissingRequestValueException e) {
        LOGGER.info(e.getMessage());
        return new ErrorMessage(apiVersion, HttpStatus.BAD_REQUEST.value(), e.getMessage(), MissingRequestValueException.class.getSimpleName());
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