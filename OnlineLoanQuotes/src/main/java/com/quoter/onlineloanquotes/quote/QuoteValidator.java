package com.quoter.onlineloanquotes.quote;

import com.quoter.onlineloanquotes.lender.Lender;

import java.util.List;

public class QuoteValidator {
    public static boolean canProduceQuote(List<Lender> lenders, int amountRequested) {
        int availableFunds = lenders
                .stream()
                .mapToInt(Lender::getAvailableFunds)
                .sum();

        return availableFunds >= amountRequested;
    }
}
