package com.tiurinvalery.springdata.sdk2.example.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.net.URI;

@Configuration
public class AbstractDynamoDBConfig {

    @Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;

    @Value("${amazon.aws.signing.region:us-west-2}")
    private String amazonSigningRegion;

    @Value("${amazon.aws.dynamodb.endpoint:http://localhost:8000}")
    private String amazonDynamoEndpoint;


    @Bean
    public DynamoDbAsyncClient dynamoDbAsyncClient() {
        return DynamoDbAsyncClient.builder()
                .endpointOverride(URI.create(amazonDynamoEndpoint))
                .credentialsProvider(awsCredentialsProvider())
                .region(Region.of(amazonSigningRegion))
                .build();
    }

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider(){
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(amazonAWSAccessKey,amazonAWSSecretKey));
    }
}
