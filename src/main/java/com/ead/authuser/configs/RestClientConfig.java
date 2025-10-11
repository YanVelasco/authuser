package com.ead.authuser.configs;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Configuration
public class RestClientConfig {

    static final int TIMEOUT = 5000;

    @LoadBalanced
    @Bean
    public RestClient.Builder getRestClientBuilder() {
        return RestClient.builder().requestFactory(getRequestFactory());
    }

    private ClientHttpRequestFactory getRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(TIMEOUT);
        factory.setReadTimeout(TIMEOUT);
        return factory;
    }

}
