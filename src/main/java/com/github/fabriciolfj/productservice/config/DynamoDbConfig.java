package com.github.fabriciolfj.productservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.net.URI;

@Configuration
public class DynamoDbConfig {

    @Value("${aws.dynamodb.url}")
    private String url;

    @Bean
    public DynamoDbAsyncClient asyncClient() {
        return DynamoDbAsyncClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create("default"))
                .endpointOverride(URI.create(url))
                .build();
    }

    @Bean
    public DynamoDbEnhancedAsyncClient productDynamoDbAsyncTable() {
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(asyncClient())
                .build();
    }

}
