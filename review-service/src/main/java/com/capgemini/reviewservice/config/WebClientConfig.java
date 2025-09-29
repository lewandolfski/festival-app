package com.capgemini.reviewservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration for WebClient used to communicate with external services.
 */
@Configuration
public class WebClientConfig {

    /**
     * Create a WebClient.Builder bean for dependency injection.
     * This allows for easy testing and configuration of WebClient instances.
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
