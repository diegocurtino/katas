package com.quoter.onlineloanquotes.lender;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LenderFileManager {
    private static final String LENDERS_FILENAME = "lenders.csv";

    public static List<Lender> loadLendersData() throws IOException, URISyntaxException {

        // TODO: Let's start small with a specific file in a specific location. Later, we can make it specified via the API.
        Path lenderFilePath = Paths.get(LenderFileManager.class.getClassLoader().getResource(LENDERS_FILENAME).toURI());
        List<Lender> lenders = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(lenderFilePath, StandardCharsets.UTF_8)) {
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