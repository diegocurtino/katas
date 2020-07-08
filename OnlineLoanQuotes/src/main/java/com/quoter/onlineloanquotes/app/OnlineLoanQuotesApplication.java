package com.quoter.onlineloanquotes.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// Needed because the scanner scans in the current package and children of it. Not adding this, causes the RESTController
// to not be found and strange error in the console
@ComponentScan(basePackages = "com.quoter.onlineloanquotes")
public class OnlineLoanQuotesApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineLoanQuotesApplication.class, args);
    }
}
