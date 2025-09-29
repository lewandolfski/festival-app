package com.capgemini.reviewservice.service;

import com.capgemini.reviewservice.dto.ReviewDto;
import com.capgemini.reviewservice.entity.Review;
import com.capgemini.reviewservice.exception.ResourceNotFoundException;
import com.capgemini.reviewservice.exception.BadRequestException;
import com.capgemini.reviewservice.mapper.ReviewMapper;
import com.capgemini.reviewservice.repository.ReviewRepository;
import com.capgemini.reviewservice.service.external.FestivalServiceClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalDouble;

/**
 * Service implementation for Review business logic.
 * Handles all review-related operations including validation and external service communication.
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final FestivalServiceClient festivalServiceClient;

    public ReviewServiceImpl(ReviewRepository reviewRepository, 
                           ReviewMapper reviewMapper,
                           FestivalServiceClient festivalServiceClient) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.festivalServiceClient = festivalServiceClient;
    }

    @Override
    public Review create(ReviewDto reviewDto) {
        // Validate that the subject exists in the Festival Application
        if (!validateSubjectExists(reviewDto.getSubjectId(), reviewDto.getSubjectType())) {
            throw new BadRequestException("Subject with ID " + reviewDto.getSubjectId() + 
                                        " and type " + reviewDto.getSubjectType() + " does not exist");
        }

        Review review = reviewMapper.toEntity(reviewDto);
        return reviewRepository.save(review);
    }

    @Override
    public Review update(String id, ReviewDto reviewDto) {
        Review existingReview = getById(id);
        
        // Validate that the subject exists if it's being changed
        if (!existingReview.getSubjectId().equals(reviewDto.getSubjectId()) || 
            !existingReview.getSubjectType().equals(reviewDto.getSubjectType())) {
            if (!validateSubjectExists(reviewDto.getSubjectId(), reviewDto.getSubjectType())) {
                throw new BadRequestException("Subject with ID " + reviewDto.getSubjectId() + 
                                            " and type " + reviewDto.getSubjectType() + " does not exist");
            }
        }

        reviewMapper.updateEntityFromDto(reviewDto, existingReview);
        existingReview.updateTimestamp();
        return reviewRepository.save(existingReview);
    }

    @Override
    public void delete(String id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Review not found with ID: " + id);
        }
        reviewRepository.deleteById(id);
    }

    @Override
    public Review getById(String id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));
    }

    @Override
    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> getReviewsForSubject(String subjectId, String subjectType) {
        return reviewRepository.findBySubjectIdAndSubjectType(subjectId, subjectType);
    }

    @Override
    public List<Review> getReviewsBySubjectType(String subjectType) {
        return reviewRepository.findBySubjectType(subjectType);
    }

    @Override
    public List<Review> getReviewsByReviewer(String reviewerName) {
        return reviewRepository.findByReviewerName(reviewerName);
    }

    @Override
    public List<Review> getReviewsByRating(Integer rating) {
        return reviewRepository.findByRating(rating);
    }

    @Override
    public List<Review> getReviewsWithMinRating(Integer rating) {
        return reviewRepository.findByRatingGreaterThanEqual(rating);
    }

    @Override
    public Double getAverageRating(String subjectId, String subjectType) {
        List<Review> reviews = reviewRepository.findBySubjectIdAndSubjectType(subjectId, subjectType);
        
        if (reviews.isEmpty()) {
            return 0.0;
        }

        OptionalDouble average = reviews.stream()
                .mapToInt(Review::getRating)
                .average();
        
        return average.orElse(0.0);
    }

    @Override
    public long getReviewCount(String subjectId, String subjectType) {
        return reviewRepository.countBySubjectIdAndSubjectType(subjectId, subjectType);
    }

    @Override
    public boolean validateSubjectExists(String subjectId, String subjectType) {
        try {
            return festivalServiceClient.subjectExists(subjectId, subjectType);
        } catch (Exception e) {
            // Log the error and assume subject doesn't exist for safety
            System.err.println("Error validating subject existence: " + e.getMessage());
            return false;
        }
    }
}
