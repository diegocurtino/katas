package app;

public class UserInputValidator {
    public static final int MINIMUM_LOAN = 100;
    private static final int MAXIMUM_LOAN = 15000;

    public static void validateAmountOfInputParameters(String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("At least one of the required parameters (lenderInfo and amount to borrow) is missing \n " +
                    "Please check how you are calling the application, eg: app.App <lendersFileName.csv> <1000>");
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
}
