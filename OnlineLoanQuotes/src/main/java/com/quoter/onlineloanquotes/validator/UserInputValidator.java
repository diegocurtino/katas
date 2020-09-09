package com.quoter.onlineloanquotes.validator;

import com.quoter.onlineloanquotes.exception.AmountException;

public class UserInputValidator {
    private static final int MINIMUM_LOAN = 100;
    private static final int MAXIMUM_LOAN = 15000;

    public static void validateAmountToBorrow(int amount) {
        if (amount < MINIMUM_LOAN) {
            throw new AmountException("The amount requested (" + amount + ") is below the minimum amount this bank loans.");
        }

        if (amount > MAXIMUM_LOAN) {
            throw new AmountException("The amount requested (" + amount + ") is above the maximum amount this bank loans.");
        }

        if (amount % 100 != 0) {
            throw new AmountException("The amount requested must be multiple of 100.");
        }
    }
}
