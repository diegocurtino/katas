package com.quoter.onlineloanquotes.controller;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping({OnlineLoanQuotesErrorController.ERROR_PATH})
public class OnlineLoanQuotesErrorController extends AbstractErrorController {

    static final String ERROR_PATH = "/error";

    public OnlineLoanQuotesErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes, Collections.emptyList());
    }

    // Added to handle the whitelabel error page. With this, it is not shown.
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, false);
        HttpStatus status = getStatus(request);
        return new ResponseEntity<>(body, status);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
