package com.quoter.onlineloanquotes.quote;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.quoter.onlineloanquotes.lender.Lender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.List;

@Data
@ApiModel
public class Quote {
    private static final Currency DEFAULT_CURRENCY = Currency.getInstance("GBP");
    private static final int SCALE_BIG_DECIMAL_VALUES = 6;

    private static final int AMOUNT_OF_MONTHLY_INSTALLMENTS = 36;
    private static final int TIMES_INTEREST_IS_COMPOUNDED_PER_YEAR = 12;

    @JsonIgnore
    private double monthlyPayment;

    @JsonIgnore
    private double averageAnnualPercentageRate;

    @JsonIgnore
    private double annualInterestRate;

    @JsonIgnore
    private int amountRequested;

    public Quote(List<Lender> lenders, int amount) {
        amountRequested = amount;
        averageAnnualPercentageRate = calculateAverageAnnualPercentageRate(lenders);
        annualInterestRate = calculateAnnualInterestRate();
        monthlyPayment = calculateMonthlyInstallment();
    }

    /**
     * Calculates a average annual percentage rate (APR) a borrower will be charged for a loan.
     *
     * @param lenders list of lenders with the necessary information to perform Average APR's calculation
     * @return the average APR calculated from lenders' information
     */
    private double calculateAverageAnnualPercentageRate(List<Lender> lenders) {
        int sumLeftToGather = amountRequested;

        int amountOfLendersNeeded = 0;
        BigDecimal sumInterestRates = BigDecimal.ZERO;

        for (Lender lender : lenders) {
            amountOfLendersNeeded += 1;
            sumInterestRates = sumInterestRates.add(lender.getRate());

            int availableFunds = lender.getAvailableFunds();

            if (sumLeftToGather <= availableFunds) {
                break;
            }

            if (sumLeftToGather > availableFunds) {
                sumLeftToGather -= availableFunds;
            }
        }

        return sumInterestRates.divide(new BigDecimal(amountOfLendersNeeded), SCALE_BIG_DECIMAL_VALUES, RoundingMode.HALF_EVEN)
                .doubleValue();
    }

    /**
     * Calculates the amount of money that a borrower must pay every month so that his/her loan is cancelled after
     * {@link #AMOUNT_OF_MONTHLY_INSTALLMENTS} payments have been made.
     * <p>
     * The formula to calculate monthly installments is M = P [i(1 + i)^n]/ {[(1 + i)^n] - 1} where
     * <p>
     * M = what the borrower will pay every month,
     * P = is the amount borrowed,
     * i = annualInterestRate / amount of times the interest is compounded per year ({@link #TIMES_INTEREST_IS_COMPOUNDED_PER_YEAR})
     * n = total number of payments to cancel the loan (in this case: 3 * 12).
     * <p>
     * See further details about the formula at: http://www.fonerbooks.com/interest.htm
     *
     * @return the monthly payment a borrower is required to do to cancel a loan on term.
     */
    private double calculateMonthlyInstallment() {
        double i = annualInterestRate / TIMES_INTEREST_IS_COMPOUNDED_PER_YEAR;

        double exponentiation = Math.pow(1 + i, AMOUNT_OF_MONTHLY_INSTALLMENTS);

        double numerator = i * exponentiation;
        double denominator = exponentiation - 1;

        return amountRequested * (numerator / denominator);
    }

    /**
     * Calculates the annual interest rate charged for a loan.
     * <p>
     * The formula to get the interest rate from APR's formula is i = q[(1+r)^(1/q) -1] where:
     * r = is APR's value
     * q = amount of times the interest is compounded per year ({@link #TIMES_INTEREST_IS_COMPOUNDED_PER_YEAR}
     * <p>
     * See further details about the formula at: http://mathforum.org/dr.math/faq/faq.interest.html#apr.
     *
     * @return the annual interest rate charged for a loan.
     */
    private double calculateAnnualInterestRate() {
        double base = (1 + averageAnnualPercentageRate);
        double exponent = 1d / TIMES_INTEREST_IS_COMPOUNDED_PER_YEAR;
        double exponentiation = Math.pow(base, exponent);

        return TIMES_INTEREST_IS_COMPOUNDED_PER_YEAR * (exponentiation - 1);
    }

    /**
     * Formats APR's value as a percentage value rounded to one decimal position.
     * <p>
     * Don't use this value is you intend to do calculation requiring precision. The value returned by this method lacks
     * it since the formatter will round it. This method is intended to be used with display purposes.
     *
     * @return the APR's formatted as percentage rounded to one decimal position.
     */
    @ApiModelProperty(value = "Annual Interest Rate as Percentage", required = true, example = "7.0%")
    @JsonProperty("interestRate")
    public String getAnnualPercentageRateAsPercentage() {
        DecimalFormat df = new DecimalFormat("0.0%");
        df.setRoundingMode(RoundingMode.HALF_EVEN);
        return df.format(averageAnnualPercentageRate);
    }

    /**
     * Formats monthly installment's amount as a 2 decimal positions rounded number
     * <p>
     * Don't use this value is you intend to do calculation requiring precision. The value returned by this method lacks
     * it since the formatter will round it. This method is intended to be used with display purposes.
     *
     * @return loan's monthly installment rounded to 2 decimal positions.
     */
    @ApiModelProperty(value = "Amount to pay monthly", required = true, example = "30.78")
    public String getMonthlyInstallment() {
        DecimalFormat df = new DecimalFormat("##.00");
        df.setRoundingMode(RoundingMode.HALF_EVEN);
        return df.format(monthlyPayment);
    }

    /**
     * Calculates the total amount a borrower will have payed after the last payment is made.
     * <p>
     * Don't use this value is you intend to do calculation requiring precision. The value returned by this method lacks
     * it since (A) it uses ({@link #getMonthlyInstallment()}) as input which is itself a rounded value and (B) the
     * the formatter will round the final value again.
     * <p>
     * The reason to calculate this value from a rounded value instead of the real one is to be able to display a value
     * that equals to the value that the borrower would obtain by manually multiplying the displayed monthly installment
     * times the {@link #AMOUNT_OF_MONTHLY_INSTALLMENTS}.
     * <p>
     * This method is intended to be used with display purposes.
     *
     * @return the total sum paid for a loan (interest included) rounded as a 2 decimal positions number.
     */

    @ApiModelProperty(value = "Total amount to be paid after the last payment is made", required = true, example = "1108")
    public String getTotalRepayment() {
        DecimalFormat df = new DecimalFormat("#####.00");
        df.setRoundingMode(RoundingMode.HALF_EVEN);

        double roundedValue = Double.parseDouble(getMonthlyInstallment()) * AMOUNT_OF_MONTHLY_INSTALLMENTS;
        return df.format(roundedValue);
    }

    @ApiModelProperty(value = "Amount to borrow", required = true, example = "1400")
    public int getAmountBorrowed() {
        return amountRequested;
    }
}
