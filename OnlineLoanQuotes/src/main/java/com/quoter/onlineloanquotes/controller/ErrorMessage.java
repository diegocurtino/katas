package com.quoter.onlineloanquotes.controller;

public record ErrorMessage(String apiVersion, int code, String message, String reason) {
}
