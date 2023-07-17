package com.quoter.onlineloanquotes.elasticsearch.importer;

import com.quoter.onlineloanquotes.lender.Lender;
import com.quoter.onlineloanquotes.lender.LenderFileManager;
import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElasticSearchImporter {
    private static final Logger LOGGER = LogManager.getLogger(ElasticSearchImporter.class);
    private static final String INDEX_NAME = "lenders";

    public static void main(String[] args) throws IOException {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http"));
        RestHighLevelClient client = new RestHighLevelClient(builder);

        List<Lender> lenders = LenderFileManager.loadLendersData();
        for (Lender lender : lenders) {
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("Lender", lender.name());
            jsonMap.put("Rate", lender.rate());
            jsonMap.put("Available", lender.availableFunds());

            IndexRequest request = new IndexRequest(ElasticSearchImporter.INDEX_NAME).source(jsonMap);

            // It seems that it's not possible to do async execution if the client is closed right after that (which makes sense)
            //client.indexAsync(request, RequestOptions.DEFAULT, listenerName);
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            LOGGER.info("Result {}. Lender {}", response.getResult(), lender);
        }
        client.close();
    }
}
