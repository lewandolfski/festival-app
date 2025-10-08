package com.capgemini.reviewservice.controller;

import com.capgemini.reviewservice.dto.ReviewDto;
import com.capgemini.reviewservice.entity.Review;
import com.capgemini.reviewservice.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Review operations.
 * Provides endpoints for managing reviews of DJs and Performances.
 */
@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*") // Allow cross-origin requests for microservice communication
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Create a new review.
     */
    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody ReviewDto reviewDto) {
        Review createdReview = reviewService.create(reviewDto);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    /**
     * Update an existing review.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable String id, @Valid @RequestBody ReviewDto reviewDto) {
        Review updatedReview = reviewService.update(id, reviewDto);
        return ResponseEntity.ok(updatedReview);
    }

    /**
     * Delete a review.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get a review by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable String id) {
        Review review = reviewService.getById(id);
        return ResponseEntity.ok(review);
    }

    /**
     * Get all reviews.
     */
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAll();
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get reviews for a specific subject (DJ or Performance).
     */
    @GetMapping("/subject/{subjectType}/{subjectId}")
    public ResponseEntity<List<Review>> getReviewsForSubject(
            @PathVariable String subjectType,
            @PathVariable String subjectId) {
        List<Review> reviews = reviewService.getReviewsForSubject(subjectId, subjectType.toUpperCase());
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get reviews by subject type (all DJ reviews or all Performance reviews).
     */
    @GetMapping("/type/{subjectType}")
    public ResponseEntity<List<Review>> getReviewsBySubjectType(@PathVariable String subjectType) {
        List<Review> reviews = reviewService.getReviewsBySubjectType(subjectType.toUpperCase());
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get reviews by reviewer name.
     */
    @GetMapping("/reviewer/{reviewerName}")
    public ResponseEntity<List<Review>> getReviewsByReviewer(@PathVariable String reviewerName) {
        List<Review> reviews = reviewService.getReviewsByReviewer(reviewerName);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get reviews by rating.
     */
    @GetMapping("/rating/{rating}")
    public ResponseEntity<List<Review>> getReviewsByRating(@PathVariable Integer rating) {
        List<Review> reviews = reviewService.getReviewsByRating(rating);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get reviews with minimum rating.
     */
    @GetMapping("/rating/min/{minRating}")
    public ResponseEntity<List<Review>> getReviewsWithMinRating(@PathVariable Integer minRating) {
        List<Review> reviews = reviewService.getReviewsWithMinRating(minRating);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get statistics for a specific subject.
     */
    @GetMapping("/stats/{subjectType}/{subjectId}")
    public ResponseEntity<Map<String, Object>> getSubjectStats(
            @PathVariable String subjectType,
            @PathVariable String subjectId) {
        
        String upperSubjectType = subjectType.toUpperCase();
        Double averageRating = reviewService.getAverageRating(subjectId, upperSubjectType);
        long reviewCount = reviewService.getReviewCount(subjectId, upperSubjectType);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("subjectId", subjectId);
        stats.put("subjectType", upperSubjectType);
        stats.put("averageRating", Math.round(averageRating * 100.0) / 100.0); // Round to 2 decimal places
        stats.put("reviewCount", reviewCount);
        
        return ResponseEntity.ok(stats);
    }

    /**
     * Health check endpoint.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "Review Service");
        return ResponseEntity.ok(status);
    }
}
