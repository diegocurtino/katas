package com.quoter.onlineloanquotes.lender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LenderFileManager {
    private static final Logger LOGGER = LogManager.getLogger(LenderFileManager.class);

    public static List<Lender> loadLendersData(String filename) throws URISyntaxException, IOException {
        List<Lender> lenders = new ArrayList<>();

        try {
            Path path = Paths.get(LenderFileManager.class.getClassLoader().getResource(filename).toURI());
            Stream<String> linesInFile = Files.lines(path);
            linesInFile.forEach(lender -> {
                String[] data = lender.split(",");
                if (LenderValidator.isLenderDataValid(data)) {
                    lenders.add(new Lender(data[0].strip(), new BigDecimal(data[1]), Integer.parseInt(data[2])));
                }
            });
        } catch (NullPointerException e) {
            LOGGER.error("Lenders file with name {} couldn't be read", filename);
            throw e;
        }

        return lenders;
    }
}