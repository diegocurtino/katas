package com.quoter.onlineloanquotes.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ElasticSearchConfiguration {
    // I can either set the properties using the @Value annotation or through the environment object. In this case, I'll
    // use the former.
    @Value("${elastic.hostname}")
    private String hostname;

    @Value("${elastic.port}")
    private int port;

    @Value("${elastic.schema}")
    private String scheme;

    @Autowired
    private Environment environment;

    @Bean
    public RestHighLevelClient elasticSearchClient() {
        /*
        return new RestHighLevelClient(RestClient.builder(new HttpHost(
                environment.getProperty("elastic.hostname"),
                Integer.parseInt(environment.getProperty("elastic.port")),
                environment.getProperty("elastic.schema"))));
        */

        return new RestHighLevelClient(RestClient.builder(new HttpHost(hostname, port, scheme)));
    }
}
