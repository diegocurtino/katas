package com.quoter.onlineloanquotes.controller;

import com.quoter.onlineloanquotes.exception.AmountException;
import com.quoter.onlineloanquotes.lender.LenderElasticSearchManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.quoter.onlineloanquotes.controller.TestData.API_VERSION_WITH_REACTIVE_CAPABILITIES;

// TODO: Update the javadocs. They do not seem to completely apply when it comes to test using webTestClient.
/**
 * From: <a href="https://thepracticaldeveloper.com/guide-spring-boot-controller-tests/">...</a>
 * <p>
 * This type of testing (with context) does not require a setup method that stand-alone tests do. Here, the controller
 * advice is injected automatically. If there were any filters, they too would be injected automatically.
 */
@WebFluxTest(QuotationController.class)
public class QuotationControllerMockMvcWithContextTest {
    @MockBean
    private LenderElasticSearchManager lenderElasticSearchManager;

    @Autowired
    private WebTestClient webTestClient;

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
}
