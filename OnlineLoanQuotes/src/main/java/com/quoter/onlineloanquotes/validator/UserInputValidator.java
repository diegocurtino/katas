package com.quoter.onlineloanquotes.validator;

import com.quoter.onlineloanquotes.exception.AmountException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserInputValidator {
    private static final Logger LOGGER = LogManager.getLogger(UserInputValidator.class);
    private static final int MINIMUM_LOAN = 100;
    private static final int MAXIMUM_LOAN = 15000;

    public static void validateAmountToBorrow(int transactionId, int amount) {
        if (amount < MINIMUM_LOAN) {
            String message = "The amount requested (" + amount + ") is below the minimum amount (" + MINIMUM_LOAN + ") this bank loans.";
            LOGGER.info("TransactionId {}. {}", transactionId, message);
            throw new AmountException(message);
        }

        if (amount > MAXIMUM_LOAN) {
            String message = "The amount requested (" + amount + ") is above the maximum amount (" + MAXIMUM_LOAN + ") this bank loans.";
            LOGGER.info("TransactionId {}. {}", transactionId, message);
            throw new AmountException(message);
        }

        if (amount % 100 != 0) {
            String message = "The amount requested must be multiple of 100.";
            LOGGER.info("TransactionId {}. {}", transactionId, message);
            throw new AmountException(message);
        }
    }
}
