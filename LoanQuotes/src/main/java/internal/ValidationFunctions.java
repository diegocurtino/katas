package internal;

import quotation.Lender;

import java.util.List;

public final class ValidationFunctions {
    public static final int MINIMUM_LOAN = 100;
    private static final int MAXIMUM_LOAN = 15000;

    public static void validateAmountOfInputParameters(String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("At least one of the required parameters (lenderInfo and amount to borrow) is missing \n " +
                    "Please check how you are calling the application, eg: quotation.LoanQuote <lendersFileName.csv> <1000>");
        }
    }

    public static void validateAmountToBorrowValue(String inputValue) {
        int amount;

        try {
            amount = Integer.parseInt(inputValue);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("The value specified as amount to borrow (" + inputValue + ") is not a number. Please check the value.");
        }

        // We could add validation for negative or 0 values but IMO that goes directly into over-designing since this
        // validation should catch those cases.
        if (amount < MINIMUM_LOAN) {
            throw new IllegalArgumentException("The amount requested (" + amount + ") is below the minimum amount this bank loans.");
        }

        if (amount > MAXIMUM_LOAN) {
            throw new IllegalArgumentException("The amount requested (" + amount + ") is above the maximum amount this bank loans.");
        }

        if (amount % 100 != 0) {
            throw new IllegalArgumentException("The amount requested must be multiple of 100.");
        }
    }

    /**
     * Determines whether individual's lender data is considered "usable"/"valid".
     * <p>
     * This method is not placed in Lender's class to be able to validate lender's data before attempting creating one.
     * The alternative would be to throw from the constructor and handle the exception which I prefer to avoid.
     * <p>
     * Lender's data will be considered unusable/invalid if ANY of the following conditions are met:
     * <p>
     * 1. No lender's name is found: the assumption is that a lender must have a name to know who should be contacted
     * to ask the money from.
     * 2. The value stated as rate can't be parsed as a double (e.g. a value using ; instead of . as decimal separator)
     * 3. The value stated as available funds is not a number.
     * 4. The amount of available money is <= 0: the assumption is that a lender with no available money is for use
     * case's all intents and purposes useless).
     *
     * @param lenderData a String[] of 3 values where lender's name is [0], rate [1] and available funds [2]
     * @return true, if lender's data is consider usable; false, otherwise.
     */
    public static boolean isLenderDataValid(String[] lenderData) {
        if (lenderData[0].trim().isEmpty()) {
            return false;
        }

        try {
            Double.parseDouble(lenderData[1]);
        } catch (NumberFormatException e) {
            return false;
        }

        int amount;
        try {
            amount = Integer.parseInt(lenderData[2]);
            if (amount > 0) {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return false;
    }

    // Useful when debugging
    public static void printLendersData(List<Lender> lenders) {
        lenders.forEach(l -> System.out.println(l.toString()));
    }
}
