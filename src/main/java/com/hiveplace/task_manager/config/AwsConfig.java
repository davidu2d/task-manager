package com.hiveplace.task_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@Configuration
public class AwsConfig {

    @Bean
    public AwsBasicCredentials awsCreds(){
        return AwsBasicCredentials.create("localstack", "localstack");
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create("http://s3.localhost.localstack.cloud:4566"))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds()))
                .build();
    }

    @Bean
    public SqsAsyncClient sqsClient() {
        return SqsAsyncClient.builder()
                .endpointOverride(URI.create("http://sqs.us-east-1.localhost.localstack.cloud:4566"))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds()))
                .build();
    }
}