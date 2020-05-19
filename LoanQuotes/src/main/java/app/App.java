package app;

import lender.Lender;
import lender.LenderFileManager;
import quote.Quote;

import java.io.IOException;
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

    private static boolean canProduceQuote(List<Lender> lenders, int amountRequested) {
        int availableFunds = lenders
                .stream()
                .mapToInt(Lender::getAvailableFunds)
                .sum();

        return availableFunds >= amountRequested;
    }

    private static void displayQuoteInfo(Quote quote) {
        String currencySymbol = Quote.getDefaultCurrency().getSymbol();

        System.out.println("Requested amount: " + currencySymbol + quote.getAmountRequested());
        System.out.println("Rate: " + quote.getAnnualPercentageRateAsPercentage());
        System.out.println("Monthly repayment: " + currencySymbol + quote.getMonthlyInstallment());
        System.out.println("Total repayment: " + currencySymbol + quote.getTotalRepayment());
    }
}