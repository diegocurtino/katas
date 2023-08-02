package com.quoter.onlineloanquotes.controller;

import com.quoter.onlineloanquotes.exception.QuoteException;
import com.quoter.onlineloanquotes.exception.SourceException;
import com.quoter.onlineloanquotes.lender.Lender;
import com.quoter.onlineloanquotes.lender.LenderElasticSearchManager;
import com.quoter.onlineloanquotes.lender.LenderFileManager;
import com.quoter.onlineloanquotes.quote.Quote;
import com.quoter.onlineloanquotes.validator.UserInputValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;

@RestController
@Validated // Added to tell Spring to evaluate the constraint annotations on method parameters
public class QuotationController {
    private static final Logger LOGGER = LogManager.getLogger(QuotationController.class);
    private static final Random RANDOM_GENERATOR = new Random();

    @Autowired
    private LenderElasticSearchManager lenderElasticSearchManager;

    private static Source validateAndGetSource(String lendersSource) {
        try {
            return Source.valueOf(lendersSource);
        } catch (IllegalArgumentException e) {
            throw new SourceException(e.getMessage());
        }
    }

    @Operation(summary = "Retrieve an online quote for a loan")
    @GetMapping(value = "/quote", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quote could be produced"),
            @ApiResponse(responseCode = "400", description = "Thee specified amount is not valid"),
            @ApiResponse(responseCode = "500", description = "There is a problem to produce a quote")
    })
    public Mono<Quote> getQuote(@Parameter(description = "Amount requested") @RequestParam @NotNull int amountRequested,
                                @Parameter(description = "Lenders source") @RequestParam @NotNull String lendersSource,
                                @Parameter(description = "Lenders filename") @RequestParam @NotNull String filename)
            throws IOException, URISyntaxException {

        int transactionId = QuotationController.RANDOM_GENERATOR.ints(0, Integer.MAX_VALUE).findFirst().getAsInt();
        QuotationController.LOGGER.info("TransactionId {}. Quote requested: {}", transactionId, amountRequested);

        UserInputValidator.validateAmountToBorrow(transactionId, amountRequested);
        Source source = QuotationController.validateAndGetSource(lendersSource.toUpperCase());

        List<Lender> lenders = switch (source) {
            case CSV -> LenderFileManager.loadLendersData(filename);
            case ELASTIC_SEARCH -> lenderElasticSearchManager.loadLendersData();
        };

        lenders.sort(Lender::compareTo);
        return produceQuote(amountRequested, lenders, transactionId);
    }

    private static Mono<Quote> produceQuote(int amountRequested, List<Lender> lenders, int transactionId) {
        if (Quote.canProduceQuote(lenders, amountRequested)) {
            Mono<Quote> quote = Mono.just(new Quote(lenders, amountRequested));
            QuotationController.LOGGER.info("TransactionId {}. Quote produced: {}", transactionId, quote);
            return quote;
        }

        String message = "There are not enough funds to produce a quote for the amount (" + amountRequested + ") requested";
        LOGGER.info("TransactionId {} .{}", transactionId, message);
        return Mono.error(new QuoteException(message));
    }
}
