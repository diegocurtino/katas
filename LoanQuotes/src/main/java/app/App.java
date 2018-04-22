package app;

import lender.Lender;
import lender.LenderValidator;
import quote.Quote;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException {
        validateUserInput(args);
        List<Lender> lenders = loadLendersData(args[0]);

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

    private static List<Lender> loadLendersData(String arg) throws IOException {
        Path lenderFilePath = Paths.get(arg);
        List<Lender> lenders = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(lenderFilePath, StandardCharsets.UTF_8)) {
            String line = null;

            reader.readLine(); // Skip the header

            while ((line = reader.readLine()) != null) {
                String[] lenderData = line.split(",");

                if (LenderValidator.isLenderDataValid(lenderData)) {
                    lenders.add(new Lender(lenderData));
                }
            }
        }
        return lenders;
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