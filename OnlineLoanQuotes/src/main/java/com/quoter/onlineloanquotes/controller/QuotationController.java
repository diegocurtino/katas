package com.quoter.onlineloanquotes.controller;

import com.quoter.onlineloanquotes.quote.Quote;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@Validated
public class QuotationController {

    @GetMapping("/quote")
    public Quote getQuote(@RequestParam @NotNull int amountRequested) {
        //TODO: Solve error stack trace.
        //TODO: Read lenders file.
        //TODO: Consider implementing the ControllerAdvise
        System.out.println("Credit requested: " + amountRequested);

        return new Quote(amountRequested);
    }
}
