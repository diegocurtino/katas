package com.quoter.onlineloanquotes.controller;

import com.quoter.onlineloanquotes.lender.Lender;
import com.quoter.onlineloanquotes.lender.LenderFileManager;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@RestController
@Validated // Added to o tell Spring to evaluate the constraint annotations on method parameters
public class LenderController {

    private static final Logger LOGGER = LogManager.getLogger(LenderController.class);
    private static final Random RANDOM_GENERATOR = new Random();

    @ApiOperation(value = "Retrieve the list of available lenders", response = Lender[].class)
    @GetMapping(value = "/lenders", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lenders info is retrieved"),
            @ApiResponse(responseCode = "500", description = "There is a problem to retrieve lenders information")
    })
    public List<Lender> getLenders() throws IOException {
        List<Lender> lenders = LenderFileManager.loadLendersData();
        lenders.sort(Lender::compareTo);
        return lenders;
    }
}
