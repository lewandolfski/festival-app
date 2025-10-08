package com.capgemini.reviewservice.repository;

import com.capgemini.reviewservice.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MongoDB repository for Review entities.
 * Provides CRUD operations and custom queries for reviews.
 */
@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    /**
     * Find all reviews for a specific subject (DJ or Performance).
     *
     * @param subjectId the ID of the subject (DJ or Performance)
     * @param subjectType the type of subject ("DJ" or "PERFORMANCE")
     * @return list of reviews for the subject
     */
    List<Review> findBySubjectIdAndSubjectType(String subjectId, String subjectType);

    /**
     * Find all reviews by subject type.
     *
     * @param subjectType the type of subject ("DJ" or "PERFORMANCE")
     * @return list of reviews for the subject type
     */
    List<Review> findBySubjectType(String subjectType);

    /**
     * Find all reviews by a specific reviewer.
     *
     * @param reviewerName the name of the reviewer
     * @return list of reviews by the reviewer
     */
    List<Review> findByReviewerName(String reviewerName);

    /**
     * Find all reviews with a specific rating.
     *
     * @param rating the rating (1-5)
     * @return list of reviews with the specified rating
     */
    List<Review> findByRating(Integer rating);

    /**
     * Find all reviews with rating greater than or equal to specified value.
     *
     * @param rating the minimum rating
     * @return list of reviews with rating >= specified value
     */
    List<Review> findByRatingGreaterThanEqual(Integer rating);

    /**
     * Count reviews for a specific subject.
     *
     * @param subjectId the ID of the subject
     * @param subjectType the type of subject
     * @return number of reviews for the subject
     */
    long countBySubjectIdAndSubjectType(String subjectId, String subjectType);

    /**
     * Calculate average rating for a specific subject.
     *
     * @param subjectId the ID of the subject
     * @param subjectType the type of subject
     * @return average rating for the subject
     */
    @Query("{ 'subjectId': ?0, 'subjectType': ?1 }")
    List<Review> findReviewsForAverageRating(String subjectId, String subjectType);
}
