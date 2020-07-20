package com.quoter.onlineloanquotes.controller;

import com.quoter.onlineloanquotes.quote.Quote;
import com.quoter.onlineloanquotes.validators.UserInputValidator;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@Validated
public class QuotationController {

    @GetMapping(value = "/quote", produces = MediaType.APPLICATION_JSON_VALUE)
    public Quote getQuote(@RequestParam @NotNull int amountRequested) {
        UserInputValidator.validateAmountToBorrow(amountRequested);
        return new Quote(amountRequested, "John Doe");
    }
}
