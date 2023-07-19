package com.quoter.onlineloanquotes.controller;

import com.quoter.onlineloanquotes.lender.Lender;
import com.quoter.onlineloanquotes.lender.LenderFileManager;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;

@RestController
public class LenderController {

    private static final Logger LOGGER = LogManager.getLogger(LenderController.class);
    private static final Random RANDOM_GENERATOR = new Random();

    @ApiOperation(value = "Retrieve the list of available lenders", response = Lender[].class)
    @GetMapping(value = "/lenders", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lenders info is retrieved"),
            @ApiResponse(responseCode = "500", description = "There is a problem to retrieve lenders information")
    })
    public List<Lender> getLenders(@ApiParam(value = "Lenders filename") @RequestParam @NotNull String filename) throws IOException, URISyntaxException {
        int transactionId = LenderController.RANDOM_GENERATOR.ints(0, Integer.MAX_VALUE).findFirst().getAsInt();
        LenderController.LOGGER.info("TransactionId {}", transactionId);

        // For the moment, we assume that the lender's file exists as app's resource. Meaning that the user must know the name of an existing file.
        // We could load the file from the user's PC but that's probably not a good idea since it must be the app who controls such data. As an
        // experiment, it can be fun to implement.
        List<Lender> lenders = LenderFileManager.loadLendersData(filename);
        lenders.sort(Lender::compareTo);
        return lenders;
    }
}
