package com.quoter.onlineloanquotes.lender;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Comparator;

public record Lender(String name, BigDecimal rate, int availableFunds) implements Comparable<Lender> {

    @Override
    public String toString() {
        return "Name: " + name + ", Rate:" + rate + ", Available funds: " + availableFunds;
    }

    @Override
    public int compareTo(@NotNull Lender lender) {
        return Comparator.comparing(Lender::rate).compare(this, lender);
    }
}