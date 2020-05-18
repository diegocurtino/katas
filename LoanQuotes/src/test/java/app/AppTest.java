package app;

import lender.Lender;
import lender.LenderFileManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import quote.Quote;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppTest {

    private static Stream<Arguments> invalidInputParametersProvider() {
        return Stream.of(
                Arguments.of(new String[]{""}, RuntimeException.class, "At least one of the required parameters (lenderInfo and amount to borrow) is missing \n" +
                        " Please check how you are calling the application, eg: app.App <lendersFileName.csv> <1000>"),
                Arguments.of(new String[]{"x.csv", "1,3"}, NumberFormatException.class, "The value specified as amount to borrow (1,3) is not a number. Please check the value."),
                Arguments.of(new String[]{"x.csv", "99"}, IllegalArgumentException.class, "The amount requested (99) is below the minimum amount this bank loans."),
                Arguments.of(new String[]{"x.csv", "15001"}, IllegalArgumentException.class, "The amount requested (15001) is above the maximum amount this bank loans."),
                Arguments.of(new String[]{"x.csv", "103"}, IllegalArgumentException.class, "The amount requested must be multiple of 100.")
        );
    }

    private static Stream<Arguments> lenderFiles() {
        return Stream.of(
                Arguments.of("lenders_one_missing_name.csv", Arrays.asList("Angela, Bob, Dave, Fred, Jane, Mary")), // John's skipped. His record doesn't have a name.
                Arguments.of("lenders_one_unavailable_to_lend.csv", Arrays.asList("Angela, Bob, Dave, Fred, Jane, John")), // Mary's skipped. She has no money to lend.
                Arguments.of("lenders_one_with_invalid_rate.csv", Arrays.asList("Angela, Dave, Fred, Jane, John, Mary")), // Bob's skipped. His rate is written with ; instead of ."
                Arguments.of("lenders_without_incorrect_info.csv", Arrays.asList("Angela, Bob, Dave, Fred, Jane, John, Mary")) // No lender is skipped
        );
    }

    private static Stream<Arguments> loanRequests() {
        return Stream.of(
                Arguments.of("lenders_without_incorrect_info.csv", 1500, true),
                Arguments.of("lenders_without_incorrect_info.csv", 2300, true),
                Arguments.of("lenders_without_incorrect_info.csv", 2400, false) // Funds available according to file: 2330.
        );
    }

    private static Stream<Arguments> quotes() {
        return Stream.of(
                // The expected average APR rates were calculated in Excel following the formulas provided in Quote class.
                Arguments.of("lenders_without_incorrect_info.csv", 1000, "0.06785", "7.0%", "30.78", "1108.08"),
                Arguments.of("lenders_without_incorrect_info.csv", 2300, "0.07521", "7.8%", "71.57", "2576.52")
        );
    }

    @DisplayName("Validate handling of invalid CLI parameters")
    @ParameterizedTest
    @MethodSource("invalidInputParametersProvider")
    void validateIncorrectInputParameters(String[] cliParameters, Class expectedExceptionClass, String expectedMessage) {
        Exception e = (Exception) assertThrows(expectedExceptionClass, () -> App.main(cliParameters));
        assertEquals(e.getMessage(), expectedMessage);
    }

    @DisplayName("Test lenders' data loading/validation")
    @ParameterizedTest
    @MethodSource("lenderFiles")
    void validateLoadOfLendersData(String lendersFilename, List<String> expectedLenderNames) throws IOException {
        List<Lender> lenders = LenderFileManager.loadLendersData("src/test/resources/" + lendersFilename);

        List<String> lenderNames = lenders
                .stream()
                .map(l -> l.getName().trim())
                .collect(toList());

        Collections.sort(lenderNames);
        assertEquals(expectedLenderNames.toString(), lenderNames.toString());
    }

    @DisplayName("Test bank capability to produce a loan's quote")
    @ParameterizedTest
    @MethodSource("loanRequests")
    void testAbilityToSatisfyLoanRequest(String lendersFilename, int amountRequested, boolean expectedResult) throws IOException {
        List<Lender> lenders = LenderFileManager.loadLendersData("src/test/resources/" + lendersFilename);

        assertEquals(expectedResult, App.canProduceQuote(lenders, amountRequested));
    }

    @DisplayName("Test successful generation of quotes")
    @ParameterizedTest
    @MethodSource("quotes")
    void testSuccessfulQuoteGeneration(String lendersFilename,
                                       int amountRequested,
                                       String expectedAnnualInterestRate,
                                       String expectedAnnualPercentageRate,
                                       String expectedMonthlyInstallment,
                                       String expectedTotalRepayment) throws IOException {

        List<Lender> lenders = LenderFileManager.loadLendersData("src/test/resources/" + lendersFilename);
        lenders.sort(Lender::compareTo);

        Quote quote = new Quote(lenders, amountRequested);

        assertEquals(expectedAnnualInterestRate, quote.getAnnualInterestRate()); // Technically not needed; good to have when something goes wrong.
        assertEquals(expectedAnnualPercentageRate, quote.getAnnualPercentageRateAsPercentage());
        assertEquals(expectedMonthlyInstallment, quote.getMonthlyInstallment());
        assertEquals(expectedTotalRepayment, quote.getTotalRepayment());
        assertEquals(amountRequested, quote.getAmountRequested());
    }
}