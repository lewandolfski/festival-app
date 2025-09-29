package com.capgemini.reviewservice.service.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;

/**
 * Client service for communicating with the Festival Application.
 * Uses WebClient to make HTTP calls to validate DJs and Performances.
 */
@Service
public class FestivalServiceClient {

    private final WebClient webClient;
    private final String festivalServiceUrl;

    public FestivalServiceClient(WebClient.Builder webClientBuilder,
                               @Value("${festival.service.url:http://localhost:9090}") String festivalServiceUrl) {
        this.festivalServiceUrl = festivalServiceUrl;
        this.webClient = webClientBuilder
                .baseUrl(festivalServiceUrl)
                .build();
    }

    /**
     * Check if a subject (DJ or Performance) exists in the Festival Application.
     *
     * @param subjectId the ID of the subject
     * @param subjectType the type of subject ("DJ" or "PERFORMANCE")
     * @return true if subject exists, false otherwise
     */
    public boolean subjectExists(String subjectId, String subjectType) {
        try {
            String endpoint = buildEndpoint(subjectType, subjectId);
            
            webClient.get()
                    .uri(endpoint)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
            
            return true; // If we get here, the subject exists
            
        } catch (WebClientResponseException.NotFound e) {
            return false; // Subject doesn't exist
        } catch (Exception e) {
            // Log error and return false for safety
            System.err.println("Error checking subject existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get subject details from the Festival Application.
     *
     * @param subjectId the ID of the subject
     * @param subjectType the type of subject ("DJ" or "PERFORMANCE")
     * @return subject details as JSON string, or null if not found
     */
    public String getSubjectDetails(String subjectId, String subjectType) {
        try {
            String endpoint = buildEndpoint(subjectType, subjectId);
            
            return webClient.get()
                    .uri(endpoint)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();
                    
        } catch (WebClientResponseException.NotFound e) {
            return null; // Subject doesn't exist
        } catch (Exception e) {
            System.err.println("Error getting subject details: " + e.getMessage());
            return null;
        }
    }

    /**
     * Build the appropriate endpoint URL based on subject type.
     *
     * @param subjectType the type of subject
     * @param subjectId the ID of the subject
     * @return the endpoint URL
     */
    private String buildEndpoint(String subjectType, String subjectId) {
        switch (subjectType.toUpperCase()) {
            case "DJ":
                return "/api/djs/" + subjectId;
            case "PERFORMANCE":
                return "/api/performances/" + subjectId;
            default:
                throw new IllegalArgumentException("Invalid subject type: " + subjectType);
        }
    }

    /**
     * Test connectivity to the Festival Application.
     *
     * @return true if Festival Application is reachable, false otherwise
     */
    public boolean isServiceAvailable() {
        try {
            webClient.get()
                    .uri("/actuator/health")
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
            
            return true;
        } catch (Exception e) {
            System.err.println("Festival service is not available: " + e.getMessage());
            return false;
        }
    }
}
