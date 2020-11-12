package com.quoter.onlineloanquotes.lender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LenderFileManager {
    private static final String LENDERS_FILENAME = "lenders.csv";

    public static List<Lender> loadLendersData() throws IOException {
        List<Lender> lenders = new ArrayList<>();

        try (InputStream is = LenderFileManager.class.getClassLoader().getResourceAsStream(LENDERS_FILENAME);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line = null;

            reader.readLine(); // Skip the header

            while ((line = reader.readLine()) != null) {
                String[] lenderData = line.split(",");

                if (LenderValidator.isLenderDataValid(lenderData)) {
                    String name = lenderData[0].trim();
                    BigDecimal rate = new BigDecimal(lenderData[1]);
                    int availableFunds = Integer.parseInt(lenderData[2]);

                    lenders.add(new Lender(name, rate, availableFunds));
                }
            }
        }
        return lenders;
    }
}