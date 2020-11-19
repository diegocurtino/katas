package com.quoter.onlineloanquotes.controller;

public enum Source {
    CSV("CSV file"),
    ELASTIC_SEARCH("ElasticSearch");

    private String name;

    Source(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
