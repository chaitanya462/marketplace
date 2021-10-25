package com.simplify.marketplace.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.simplify.marketplace.repository")
@ComponentScan(basePackages = { "com.simplify.marketplace.web.rest" })
public class ElasticSearchConfiguration {
	
	
	
	@Value("${spring.elasticSearch.custom.port}")
	String ElasticSearchPort;

    @Bean
    public RestHighLevelClient client() {
        ClientConfiguration clientconf = ClientConfiguration.builder().connectedTo(ElasticSearchPort).build();

        return RestClients.create(clientconf).rest();
    }
}
