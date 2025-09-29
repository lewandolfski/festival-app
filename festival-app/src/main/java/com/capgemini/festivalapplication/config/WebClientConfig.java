package com.capgemini.festivalapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration for WebClient used for inter-service communication.
 */
@Configuration
public class WebClientConfig {

    /**
     * Creates a WebClient.Builder bean for dependency injection.
     * This allows for customization and testing of WebClient instances.
     *
     * @return WebClient.Builder instance
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
