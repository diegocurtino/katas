package com.quoter.onlineloanquotes.concurrent;

import com.quoter.onlineloanquotes.controller.QuotationController;
import com.quoter.onlineloanquotes.controller.Source;

public class QuoteControllerRunnable implements Runnable {
    private final QuotationController controller;
    private final int amount;

    public QuoteControllerRunnable(QuotationController controller, int amount) {
        this.controller = controller;
        this.amount = amount;
    }

    @Override
    public void run() {
        try {
            controller.getQuote(amount, Source.CSV.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
