package com.quoter.onlineloanquotes.controller;

import com.quoter.onlineloanquotes.exception.AmountException;
import com.quoter.onlineloanquotes.lender.LenderElasticSearchManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * From: https://thepracticaldeveloper.com/guide-spring-boot-controller-tests/
 * <p>
 * This type of testing (with context) does not require a setup method that stand alone tests do. Here, the controller
 * advice is injected automatically. If there were any filters, they too would be injected automatically.
 */
@ExtendWith(SpringExtension.class) // Initializes a partial Spring context.
@AutoConfigureJsonTesters
@WebMvcTest(QuotationController.class) // Gets the MockMVC instance auto-configured and available in the context
public class QuotationControllerMockMvcWithContextTest {
    @MockBean
    private LenderElasticSearchManager lenderElasticSearchManager;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<ErrorMessage> errorWriter;

    @DisplayName("Validate amount requested")
    @ParameterizedTest
    @MethodSource("com.quoter.onlineloanquotes.controller.TestData#amountValues")
    public void amountRequestedIsNotValid(String amountRequested, String errorMessage) throws Exception {
        // Since the tests in this class are executed with Spring's context, apiVersion's value in app's controller
        // advise is injected and there no need to set it via ReflectionTestUnits.
        MockHttpServletResponse response = mockMvc.perform(
                 get("/quote?amountRequested=" + amountRequested + "&lendersSource=CSV&filename=" + TestData.DEFAULT_LENDERS_FILENAME)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        ErrorMessage message = new ErrorMessage("1.0", HttpStatus.BAD_REQUEST.value(), errorMessage,
                AmountException.class.getSimpleName());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo(errorWriter.write(message).getJson());
    }
}
