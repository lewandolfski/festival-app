package com.capgemini.festivalapplication.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.List;

/**
 * Client service for communicating with the Review Microservice.
 * Handles all review-related operations by making HTTP calls to the review service.
 */
@Service
public class ReviewServiceClient {

    private final WebClient webClient;
    private final String reviewServiceUrl;

    public ReviewServiceClient(WebClient.Builder webClientBuilder,
                             @Value("${review.service.url:http://localhost:8080}") String reviewServiceUrl) {
        this.reviewServiceUrl = reviewServiceUrl;
        this.webClient = webClientBuilder
                .baseUrl(reviewServiceUrl)
                .build();
    }

    /**
     * Get all reviews for a specific DJ.
     *
     * @param djId the DJ ID
     * @return JSON string containing the reviews
     */
    public String getReviewsForDJ(Long djId) {
        try {
            return webClient.get()
                    .uri("/api/reviews/subject/{subjectId}/type/{subjectType}", djId, "DJ")
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            return "[]"; // Return empty array if no reviews found
        } catch (Exception e) {
            System.err.println("Error fetching DJ reviews: " + e.getMessage());
            return "[]";
        }
    }

    /**
     * Get all reviews for a specific Performance.
     *
     * @param performanceId the Performance ID
     * @return JSON string containing the reviews
     */
    public String getReviewsForPerformance(Long performanceId) {
        try {
            return webClient.get()
                    .uri("/api/reviews/subject/{subjectId}/type/{subjectType}", performanceId, "PERFORMANCE")
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            return "[]"; // Return empty array if no reviews found
        } catch (Exception e) {
            System.err.println("Error fetching Performance reviews: " + e.getMessage());
            return "[]";
        }
    }

    /**
     * Get average rating for a DJ.
     *
     * @param djId the DJ ID
     * @return average rating as Double, or 0.0 if no reviews
     */
    public Double getAverageRatingForDJ(Long djId) {
        try {
            String response = webClient.get()
                    .uri("/api/reviews/subject/{subjectId}/type/{subjectType}/average", djId, "DJ")
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
            return Double.parseDouble(response);
        } catch (Exception e) {
            System.err.println("Error fetching DJ average rating: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Get average rating for a Performance.
     *
     * @param performanceId the Performance ID
     * @return average rating as Double, or 0.0 if no reviews
     */
    public Double getAverageRatingForPerformance(Long performanceId) {
        try {
            String response = webClient.get()
                    .uri("/api/reviews/subject/{subjectId}/type/{subjectType}/average", performanceId, "PERFORMANCE")
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
            return Double.parseDouble(response);
        } catch (Exception e) {
            System.err.println("Error fetching Performance average rating: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Get review count for a DJ.
     *
     * @param djId the DJ ID
     * @return number of reviews
     */
    public Long getReviewCountForDJ(Long djId) {
        try {
            String response = webClient.get()
                    .uri("/api/reviews/subject/{subjectId}/type/{subjectType}/count", djId, "DJ")
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
            return Long.parseLong(response);
        } catch (Exception e) {
            System.err.println("Error fetching DJ review count: " + e.getMessage());
            return 0L;
        }
    }

    /**
     * Get review count for a Performance.
     *
     * @param performanceId the Performance ID
     * @return number of reviews
     */
    public Long getReviewCountForPerformance(Long performanceId) {
        try {
            String response = webClient.get()
                    .uri("/api/reviews/subject/{subjectId}/type/{subjectType}/count", performanceId, "PERFORMANCE")
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
            return Long.parseLong(response);
        } catch (Exception e) {
            System.err.println("Error fetching Performance review count: " + e.getMessage());
            return 0L;
        }
    }

    /**
     * Test connectivity to the Review Service.
     *
     * @return true if Review Service is reachable, false otherwise
     */
    public boolean isReviewServiceAvailable() {
        try {
            webClient.get()
                    .uri("/actuator/health")
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
            return true;
        } catch (Exception e) {
            System.err.println("Review service is not available: " + e.getMessage());
            return false;
        }
    }
}
