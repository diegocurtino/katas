package com.quoter.onlineloanquotes.validator;

import com.quoter.onlineloanquotes.exception.AmountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserInputValidatorTest {

    private static Stream<Arguments> amountValues() {
        return Stream.of(
                Arguments.of(50, "The amount requested (50) is below the minimum amount this bank loans."),
                Arguments.of(15100, "The amount requested (15100) is above the maximum amount this bank loans."),
                Arguments.of(14001, "The amount requested must be multiple of 100.")
        );
    }

    @DisplayName("Validate amount requested")
    @ParameterizedTest
    @MethodSource("amountValues")
    void validateIncorrectInputParameters(int amountRequested, String expectedMessage) {
        Exception e = assertThrows(AmountException.class, () -> UserInputValidator.validateAmountToBorrow(amountRequested));
        assertEquals(e.getMessage(), expectedMessage);
    }
}
