package com.quoter.onlineloanquotes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quoter.onlineloanquotes.exceptions.AmountException;
import com.quoter.onlineloanquotes.quote.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class QuotationControllerTest {
    private MockMvc mockMvc;

    private QuotationController controller = new QuotationController();
    private QuotationControllerAdvise controllerAdvise = new QuotationControllerAdvise();

    private JacksonTester<Quote> jsonWriter;
    private JacksonTester<ErrorMessage> errorWriter;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(controllerAdvise)
                .build();
    }

    @Test
    public void canProduceQuotation() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/quote?amountRequested=500")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonWriter.write(new Quote(500)).getJson());
    }

    private static Stream<Arguments> amountValues() {
        return Stream.of(
                Arguments.of("50", "The amount requested (50) is below the minimum amount this bank loans."),
                Arguments.of("15100", "The amount requested (15100) is above the maximum amount this bank loans."),
                Arguments.of("14001", "The amount requested must be multiple of 100.")
        );
    }

    @DisplayName("Validate amount requested")
    @ParameterizedTest
    @MethodSource("amountValues")
    public void amountRequestedIsNotValid(String amountRequested, String errorMessage) throws Exception {
        String expectedApiVersion = "1.0";

        // This is done to overcome the limitation that Spring is not injecting the value for apiVersion which results
        // in a null value in the test.
        ReflectionTestUtils.setField(controllerAdvise, "apiVersion", expectedApiVersion);

        MockHttpServletResponse response = mockMvc.perform(
                get("/quote?amountRequested=" + amountRequested)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        ErrorMessage message = new ErrorMessage(expectedApiVersion, HttpStatus.BAD_REQUEST.value(), errorMessage,
                AmountException.class.getSimpleName());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo(errorWriter.write(message).getJson());
    }

    @Test
    public void mediaTypeIsNotSupported() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get("/quote?amountRequested=500")
                        .accept(MediaType.TEXT_HTML))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getContentAsString()).isEqualTo("Acceptable MIME type:" + MediaType.APPLICATION_JSON_VALUE);
    }
}
