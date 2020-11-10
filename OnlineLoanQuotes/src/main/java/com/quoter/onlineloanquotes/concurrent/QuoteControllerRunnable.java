package com.quoter.onlineloanquotes.concurrent;

import com.quoter.onlineloanquotes.controller.QuotationController;

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
            controller.getQuote(amount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
