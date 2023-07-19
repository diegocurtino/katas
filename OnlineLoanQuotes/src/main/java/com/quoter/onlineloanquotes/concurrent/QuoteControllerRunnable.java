package com.quoter.onlineloanquotes.concurrent;

import com.quoter.onlineloanquotes.controller.QuotationController;
import com.quoter.onlineloanquotes.controller.Source;

public class QuoteControllerRunnable implements Runnable {
    private final QuotationController controller;
    private final int amount;
    private final String lendersFilename;

    public QuoteControllerRunnable(QuotationController controller, int amount, String lendersFilename) {
        this.controller = controller;
        this.amount = amount;
        this.lendersFilename = lendersFilename;
    }

    @Override
    public void run() {
        try {
            controller.getQuote(amount, Source.CSV.getName(), lendersFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
