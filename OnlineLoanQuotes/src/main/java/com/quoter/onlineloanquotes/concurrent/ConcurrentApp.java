package com.quoter.onlineloanquotes.concurrent;

import com.quoter.onlineloanquotes.controller.QuotationController;
import com.quoter.onlineloanquotes.lender.Lender;
import com.quoter.onlineloanquotes.lender.LenderFileManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentApp {
    private static final Logger LOGGER = LogManager.getLogger(ConcurrentApp.class);
    private static final Random RANDOM_GENERATOR = new Random();
    private static final int AMOUNT_OF_QUOTES = 150;
    private static final AtomicInteger validQuoteAmount = new AtomicInteger(0);
    private static final AtomicInteger invalidQuoteAmount = new AtomicInteger(0);

    public static void main(String[] args) throws IOException, URISyntaxException {
        ConcurrentApp concurrentApp = new ConcurrentApp();
        concurrentApp.produceQuotes(args[0]);
    }

    /**
     * Generates an amount to quote based on a random number.
     * <p>
     * If the value x < 0.5 then a quotable value will be produced: value multiple of 100 and in between the lowest
     * quotable value (100) and the maximum amount available to quote (2331) that will be rounded down to 2300.
     * <p>
     * If the value x >= 0.5 then any integer number will be produced. Some of those numbers can be used as input to
     * produce quotes.
     */
    private static int getAmount() {
        double value = RANDOM_GENERATOR.doubles(0, 1).findFirst().getAsDouble();

        if (value < 0.5) {
            validQuoteAmount.getAndIncrement();
            return getValidAmount(100, 2331);
        }

        invalidQuoteAmount.getAndIncrement();
        return RANDOM_GENERATOR.ints(0, Integer.MAX_VALUE).findFirst().getAsInt();
    }

    private static int getValidAmount(int lowerLimit, int upperLimit) {
        int amount = RANDOM_GENERATOR.ints(lowerLimit, upperLimit).findFirst().getAsInt();

        if (amount % 100 == 0) {
            return amount;
        }
        return amount - (amount % 100);
    }

    private void produceQuotes(String lendersFilename) throws IOException, URISyntaxException {
        QuotationController Controller = new QuotationController();

        List<Lender> lenders = LenderFileManager.loadLendersData(lendersFilename);
        lenders.sort(Lender::compareTo);

        ExecutorService executor = Executors.newFixedThreadPool(150);
        for (int i = 0; i < AMOUNT_OF_QUOTES; i++) {
            Runnable runnable = new QuoteControllerRunnable(Controller, getAmount(), lendersFilename);
            Future<?> result = executor.submit(runnable);
            try {
                result.get();
            } catch (Exception e) {
                LOGGER.info(e.getMessage());
            }
        }
        executor.shutdown();
        LOGGER.info("Total valid: {}", validQuoteAmount.get());
        LOGGER.info("Total invalid: {}", invalidQuoteAmount.get());
    }
}
