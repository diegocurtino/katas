package com.quoter.onlineloanquotes.controller;

public enum Source {
    CSV("CSV file"),
    ELASTIC_SEARCH("Elastic_Search");

    private final String name;

    Source(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
