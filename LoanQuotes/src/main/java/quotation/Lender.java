package quotation;

import java.math.BigDecimal;
import java.util.Comparator;

public class Lender implements Comparable<Lender> {

    private final String name;
    private final BigDecimal rate;
    private final int availableFunds;

    public Lender(String[] lenderData) {
        name = lenderData[0].trim();
        rate = new BigDecimal(lenderData[1]);
        availableFunds = Integer.parseInt(lenderData[2]);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public int getAvailableFunds() {
        return availableFunds;
    }

    @Override
    public String toString() {
        return name + ", " + rate + ", " + availableFunds; // Use ONLY for debugging.
    }

    @Override
    public int compareTo(Lender o) {
        return Comparator.comparing(Lender::getRate).compare(this, o);
    }
}