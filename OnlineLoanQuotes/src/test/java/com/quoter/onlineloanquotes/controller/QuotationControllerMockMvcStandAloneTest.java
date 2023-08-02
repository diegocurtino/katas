package com.quoter.onlineloanquotes.controller;

import com.quoter.onlineloanquotes.exception.AmountException;
import com.quoter.onlineloanquotes.exception.QuoteException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.quoter.onlineloanquotes.controller.TestData.API_VERSION_WITH_REACTIVE_CAPABILITIES;

// TODO: Update the javadocs. They do not seem to completely apply when it comes to test using webTestClient.

/**
 * From: <a href="https://thepracticaldeveloper.com/guide-spring-boot-controller-tests/">The practical developer. How to
 * test Spring Boot controller</a>
 * <p>
 * This type of testing (stand alone) requires explicit configuration of the items used in the test (controller and
 * controller advice for instance) in the setup method.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuotationControllerMockMvcStandAloneTest {
    @Autowired
    private WebTestClient webTestClient;

    @ParameterizedTest
    @MethodSource("com.quoter.onlineloanquotes.controller.TestData#validQuotes")
    public void canProduceQuotation(int amountRequested,
                                    String expectedAnnualPercentageRate,
                                    String expectedMonthlyInstalment,
                                    String expectedTotalRepayment) {

        webTestClient.get()
                .uri("/quote?amountRequested=" + amountRequested + "&lendersSource=CSV&filename=" + TestData.DEFAULT_LENDERS_FILENAME)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.amountBorrowed").isEqualTo(amountRequested)
                .jsonPath("$.monthlyInstallment").isEqualTo(expectedMonthlyInstalment)
                .jsonPath("$.totalRepayment").isEqualTo(expectedTotalRepayment)
                .jsonPath("$.interestRate").isEqualTo(expectedAnnualPercentageRate);
    }

    @Test
    public void cannotProduceQuotation() {
        String expectedErrorMessage = "There are not enough funds to produce a quote for the amount (15000) requested";

        webTestClient.get()
                .uri("/quote?amountRequested=" + 15000 + "&lendersSource=CSV&filename=" + TestData.DEFAULT_LENDERS_FILENAME)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.apiVersion").isEqualTo(API_VERSION_WITH_REACTIVE_CAPABILITIES)
                .jsonPath("$.code").isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .jsonPath("$.message").isEqualTo(expectedErrorMessage)
                .jsonPath("$.reason").isEqualTo(QuoteException.class.getSimpleName());
    }

    @DisplayName("Validate amount requested")
    @ParameterizedTest
    @MethodSource("com.quoter.onlineloanquotes.controller.TestData#invalidAmountValues")
    public void amountRequestedIsNotValid(String amountRequested, String errorMessage) {
        webTestClient.get()
                .uri("/quote?amountRequested=" + amountRequested + "&lendersSource=CSV&filename=" + TestData.DEFAULT_LENDERS_FILENAME)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.apiVersion").isEqualTo(API_VERSION_WITH_REACTIVE_CAPABILITIES)
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo(errorMessage)
                .jsonPath("$.reason").isEqualTo(AmountException.class.getSimpleName());
    }

    @DisplayName("Validate unhandled media types are rejected")
    @Test
    public void mediaTypeIsNotSupported() {
        webTestClient.get()
                .uri("/quote?amountRequested=500&lendersSource=CSV&filename=" + TestData.DEFAULT_LENDERS_FILENAME)
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().valueMatches("Accept", "application/json")
                .expectBody().isEmpty();
    }
}
