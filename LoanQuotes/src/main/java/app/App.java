package app;

import lender.Lender;
import lender.LenderFileManager;
import quote.Quote;

import java.io.IOException;
import java.util.Currency;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException {
        validateUserInput(args);
        List<Lender> lenders = LenderFileManager.loadLendersData(args[0]);

        int amountRequested = Integer.parseInt(args[1]);
        if (canProduceQuote(lenders, amountRequested)) {
            lenders.sort(Lender::compareTo); // Only sort lender's data once we know we can actually quote the requested loan.

            Quote quote = new Quote(lenders, amountRequested);
            displayQuoteInfo(quote);
        } else {
            System.out.println("We are sorry but we are unable to provide a quote at this moment");
        }
    }

    private static void validateUserInput(String[] args) {
        UserInputValidator.validateAmountOfInputParameters(args);
        UserInputValidator.validateAmountToBorrowValue(args[1]);
    }

    public static boolean canProduceQuote(List<Lender> lenders, int amountRequested) {
        // The only reason to make it public is to make it testable directly.
        int availableFunds = lenders
                .stream()
                .mapToInt(Lender::getAvailableFunds)
                .sum();

        return availableFunds >= amountRequested;
    }

    private static void displayQuoteInfo(Quote quote) {
        Currency c = Currency.getInstance("GBP");

        System.out.println("Requested amount: " + c.getSymbol() + quote.getAmountRequested());
        System.out.println("Rate: " + quote.getAnnualPercentageRateAsPercentage());
        System.out.println("Monthly repayment: " + c.getSymbol() + quote.getMonthlyInstallment());
        System.out.println("Total repayment: " + c.getSymbol() + quote.getTotalRepayment());
    }
}