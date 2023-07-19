package com.quoter.onlineloanquotes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quoter.onlineloanquotes.exception.AmountException;
import com.quoter.onlineloanquotes.exception.QuoteException;
import com.quoter.onlineloanquotes.lender.Lender;
import com.quoter.onlineloanquotes.lender.LenderFileManager;
import com.quoter.onlineloanquotes.quote.Quote;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * From: https://thepracticaldeveloper.com/guide-spring-boot-controller-tests/
 * <p>
 * This type of testing (stand alone) requires explicit configuration of the items used in the test (controller and
 * controller advice for instance) in the setup method.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuotationControllerMockMvcStandAloneTest {
    private static final QuotationController CONTROLLER_TESTED = new QuotationController();
    private static final QuotationControllerAdvise CONTROLLER_ADVISE = new QuotationControllerAdvise();

    private MockMvc mockMvc;
    private JacksonTester<Quote> quoteWriter;
    private JacksonTester<ErrorMessage> errorWriter;

    @BeforeAll
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        // Any part of your logic that is placed outside the Controller class (e.g. ControllerAdvice, Filters) needs to
        // be configured here. The reason is that you there's no Spring context that can inject them automatically.
        mockMvc = MockMvcBuilders.standaloneSetup(CONTROLLER_TESTED)
                .setControllerAdvice(CONTROLLER_ADVISE)
                .build();
    }

    @ParameterizedTest
    @MethodSource("com.quoter.onlineloanquotes.controller.TestData#quotes")
    public void canProduceQuotation(int amountRequested,
                                    String expectedAnnualPercentageRate,
                                    String expectedMonthlyInstallment,
                                    String expectedTotalRepayment) throws Exception {

        MockHttpServletResponse response = mockMvc.perform(get("/quote?amountRequested=" + amountRequested + "&lendersSource=CSV&filename=" + TestData.DEFAULT_LENDERS_FILENAME)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        List<Lender> lenders = LenderFileManager.loadLendersData("lenders_without_incorrect_info.csv");
        lenders.sort(Lender::compareTo);

        JsonContent<Quote> quoteAsJson = quoteWriter.write(new Quote(1, lenders, amountRequested));
        String contentAsString = response.getContentAsString();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(quoteAsJson.getJson());

        assertThat(quoteAsJson).hasJsonPathValue("$.amountBorrowed", String.valueOf(amountRequested));
        assertThat(quoteAsJson).hasJsonPathValue("$.totalRepayment", expectedTotalRepayment);
        assertThat(quoteAsJson).hasJsonPathValue("$.monthlyInstallment", expectedMonthlyInstallment);
        assertThat(quoteAsJson).hasJsonPathValue("$.interestRate", expectedAnnualPercentageRate);
    }

    @Test
    public void cannotProduceQuotation() throws Exception {
        String expectedApiVersion = "1.0";
        setApiVersionValueUsingReflection(expectedApiVersion);

        String amountRequested = "15000";
        List<Lender> lenders = LenderFileManager.loadLendersData("lenders_without_incorrect_info.csv");
        lenders.sort(Lender::compareTo);

        MockHttpServletResponse response = mockMvc.perform(get("/quote?amountRequested=" + amountRequested + "&lendersSource=CSV&filename=" + TestData.DEFAULT_LENDERS_FILENAME)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        String errorMessage = "There are not enough funds to produce a quote for the amount (" + amountRequested + ") requested";

        ErrorMessage message = new ErrorMessage(expectedApiVersion, HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage,
                QuoteException.class.getSimpleName());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo(errorWriter.write(message).getJson());
    }

    private void setApiVersionValueUsingReflection(String expectedApiVersion) {
        // Since the tests in this class are executed as unit tests without Spring's context, apiVersion's value in app's
        // controller advise is not injected and it's equal to NULL. Using ReflectionTestUnits we can set a value for it
        // in spite that we shouldn't abuse of it since manipulating object via reflection is not a good practice.
        ReflectionTestUtils.setField(CONTROLLER_ADVISE, "apiVersion", expectedApiVersion);
    }

    @DisplayName("Validate amount requested")
    @ParameterizedTest
    @MethodSource("com.quoter.onlineloanquotes.controller.TestData#amountValues")
    public void amountRequestedIsNotValid(String amountRequested, String errorMessage) throws Exception {
        String expectedApiVersion = "1.0";
        setApiVersionValueUsingReflection(expectedApiVersion);

        MockHttpServletResponse response = mockMvc.perform(
                 get("/quote?amountRequested=" + amountRequested + "&lendersSource=CSV&filename=" + TestData.DEFAULT_LENDERS_FILENAME)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        ErrorMessage message = new ErrorMessage(expectedApiVersion, HttpStatus.BAD_REQUEST.value(), errorMessage,
                AmountException.class.getSimpleName());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains(errorMessage); // Do not validate the whole message (to avoid dealing with the transaction id)
    }

    @Test
    public void mediaTypeIsNotSupported() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                 get("/quote?amountRequested=500" + "&lendersSource=CSV&filename=" + TestData.DEFAULT_LENDERS_FILENAME)
                .accept(MediaType.TEXT_HTML))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getContentAsString()).isEqualTo("Acceptable MIME type:" + MediaType.APPLICATION_JSON_VALUE);
    }
}
