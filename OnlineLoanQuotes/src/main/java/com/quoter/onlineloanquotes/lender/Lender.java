package com.quoter.onlineloanquotes.lender;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;

@Data
public class Lender implements Comparable<Lender> {
    private final String name;
    private final BigDecimal rate;
    private final int availableFunds;

    @Override
    public String toString() {
        return "Name: " + name + ", Rate:" + rate + ", Available funds: " + availableFunds;
    }

    @Override
    public int compareTo(Lender lender) {
        return Comparator.comparing(Lender::getRate).compare(this, lender);
    }
}