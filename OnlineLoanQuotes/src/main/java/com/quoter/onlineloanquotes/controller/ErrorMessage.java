package com.quoter.onlineloanquotes.controller;

import lombok.Data;

@Data
public class ErrorMessage {
    private final String apiVersion;
    private final int code;
    private final String message;
    private final String reason;
}
