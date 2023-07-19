package com.quoter.onlineloanquotes.controller;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public final class TestData {
    public static final String DEFAULT_LENDERS_FILENAME = "lenders_without_incorrect_info.csv";

    public static Stream<Arguments> quotes() {
        return Stream.of(
                // The expected average APR rates were calculated in Excel following the formulas provided in Quote class.
                Arguments.of(1000, "7.0%", "30.78", "1108.08"),
                Arguments.of(2300, "7.8%", "71.57", "2576.52")
        );
    }

    public static Stream<Arguments> amountValues() {
        return Stream.of(
                Arguments.of("50", "The amount requested (50) is below the minimum amount (100) this bank loans."),
                Arguments.of("15100", "The amount requested (15100) is above the maximum amount (15000) this bank loans."),
                Arguments.of("14001", "The amount requested must be multiple of 100.")
        );
    }
}
