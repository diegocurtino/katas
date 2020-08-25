package com.quoter.onlineloanquotes.controller;

import com.quoter.onlineloanquotes.quote.Quote;
import com.quoter.onlineloanquotes.validator.UserInputValidator;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@Validated
public class QuotationController {

    @ApiOperation(value = "Retrieve an online quote for a loan", response = Quote.class)
    @GetMapping(value = "/quote", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quote could be produced"),
            @ApiResponse(responseCode = "400", description = "There is a problem with the specified amount")
    })
    public Quote getQuote(@ApiParam(value = "Amount requested") @RequestParam @NotNull int amountRequested) {
        UserInputValidator.validateAmountToBorrow(amountRequested);
        return new Quote(amountRequested);
    }
}
