package com.capgemini.reviewservice.service;

import com.capgemini.reviewservice.dto.ReviewDto;
import com.capgemini.reviewservice.entity.Review;

import java.util.List;

/**
 * Service interface for Review business logic.
 * Defines all operations available for managing reviews.
 */
public interface ReviewService {

    /**
     * Create a new review.
     *
     * @param reviewDto the review data
     * @return the created review
     */
    Review create(ReviewDto reviewDto);

    /**
     * Update an existing review.
     *
     * @param id the review ID
     * @param reviewDto the updated review data
     * @return the updated review
     */
    Review update(String id, ReviewDto reviewDto);

    /**
     * Delete a review by ID.
     *
     * @param id the review ID
     */
    void delete(String id);

    /**
     * Get a review by ID.
     *
     * @param id the review ID
     * @return the review
     */
    Review getById(String id);

    /**
     * Get all reviews.
     *
     * @return list of all reviews
     */
    List<Review> getAll();

    /**
     * Get all reviews for a specific subject (DJ or Performance).
     *
     * @param subjectId the subject ID
     * @param subjectType the subject type ("DJ" or "PERFORMANCE")
     * @return list of reviews for the subject
     */
    List<Review> getReviewsForSubject(String subjectId, String subjectType);

    /**
     * Get all reviews by subject type.
     *
     * @param subjectType the subject type ("DJ" or "PERFORMANCE")
     * @return list of reviews for the subject type
     */
    List<Review> getReviewsBySubjectType(String subjectType);

    /**
     * Get all reviews by a specific reviewer.
     *
     * @param reviewerName the reviewer name
     * @return list of reviews by the reviewer
     */
    List<Review> getReviewsByReviewer(String reviewerName);

    /**
     * Get all reviews with a specific rating.
     *
     * @param rating the rating (1-5)
     * @return list of reviews with the specified rating
     */
    List<Review> getReviewsByRating(Integer rating);

    /**
     * Get all reviews with rating greater than or equal to specified value.
     *
     * @param rating the minimum rating
     * @return list of reviews with rating >= specified value
     */
    List<Review> getReviewsWithMinRating(Integer rating);

    /**
     * Calculate average rating for a specific subject.
     *
     * @param subjectId the subject ID
     * @param subjectType the subject type
     * @return average rating for the subject
     */
    Double getAverageRating(String subjectId, String subjectType);

    /**
     * Get review count for a specific subject.
     *
     * @param subjectId the subject ID
     * @param subjectType the subject type
     * @return number of reviews for the subject
     */
    long getReviewCount(String subjectId, String subjectType);

    /**
     * Validate if a subject exists in the Festival Application.
     *
     * @param subjectId the subject ID
     * @param subjectType the subject type
     * @return true if subject exists, false otherwise
     */
    boolean validateSubjectExists(String subjectId, String subjectType);
}
