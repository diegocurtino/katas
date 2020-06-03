package lender;

import java.math.BigDecimal;
import java.util.Comparator;

public class Lender implements Comparable<Lender> {
    private final String name;
    private final BigDecimal rate;
    private final int availableFunds;

    public Lender(String name, BigDecimal rate, int availableFunds) {
        this.name = name;
        this.rate = rate;
        this.availableFunds = availableFunds;
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