package com.quoter.onlineloanquotes.lender;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class LenderElasticSearchManager {

    @Autowired
    private RestHighLevelClient elasticSearchClient;

    public List<Lender> loadLendersData() throws IOException {
        SearchRequest searchRequest = new SearchRequest("lenders");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        List<Lender> lenders = new ArrayList<>();

        SearchResponse searchResponse = elasticSearchClient.search(searchRequest, RequestOptions.DEFAULT);

        for (SearchHit hit : searchResponse.getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();

            String name = (String) sourceAsMap.get("Lender");
            BigDecimal rate = BigDecimal.valueOf((double) sourceAsMap.get("Rate"));
            int funds = (int) sourceAsMap.get("Available");

            lenders.add(new Lender(name, rate, funds));
        }
        return lenders;
    }
}

