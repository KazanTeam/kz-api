package com.kazan.config;

import com.amazonaws.ClientConfigurationFactory;
import com.amazonaws.auth.*;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClient;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfiguration {

    @Value("${aws.accesskey.id}")
    private String AWS_ACCESS_KEY;

    @Value("${aws.accesskey.secret}")
    private String AWS_ACCESS_SECRET_KEY;

    @Value("${aws.region}")
    private String AWS_REGION;

    @Bean
    public AWSCognitoIdentityProvider cognitoClient() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_ACCESS_SECRET_KEY);
        AWSCognitoIdentityProvider client = AWSCognitoIdentityProviderClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.fromName(AWS_REGION))
                .build();
        return client;
    }

}
