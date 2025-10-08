package com.capgemini.reviewservice.service;

import com.capgemini.reviewservice.dto.ReviewDto;
import com.capgemini.reviewservice.entity.Review;
import com.capgemini.reviewservice.exception.ResourceNotFoundException;
import com.capgemini.reviewservice.exception.BadRequestException;
import com.capgemini.reviewservice.mapper.ReviewMapper;
import com.capgemini.reviewservice.repository.ReviewRepository;
import com.capgemini.reviewservice.service.external.FestivalServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ReviewServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private FestivalServiceClient festivalServiceClient;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private ReviewDto testReviewDto;
    private Review testReview;

    @BeforeEach
    void setUp() {
        testReviewDto = new ReviewDto("dj-1", "DJ", "John Doe", 5, "Great performance!");
        testReview = new Review("dj-1", "DJ", "John Doe", 5, "Great performance!");
        testReview.setId("review-1");
    }

    @Test
    void shouldCreateReview() {
        // Given
        when(festivalServiceClient.subjectExists("dj-1", "DJ")).thenReturn(true);
        when(reviewMapper.toEntity(testReviewDto)).thenReturn(testReview);
        when(reviewRepository.save(testReview)).thenReturn(testReview);

        // When
        Review result = reviewService.create(testReviewDto);

        // Then
        assertNotNull(result);
        assertEquals(testReview.getId(), result.getId());
        verify(festivalServiceClient).subjectExists("dj-1", "DJ");
        verify(reviewRepository).save(testReview);
    }

    @Test
    void shouldThrowBadRequestWhenSubjectDoesNotExist() {
        // Given
        when(festivalServiceClient.subjectExists("dj-1", "DJ")).thenReturn(false);

        // When & Then
        assertThrows(BadRequestException.class, () -> reviewService.create(testReviewDto));
        verify(festivalServiceClient).subjectExists("dj-1", "DJ");
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void shouldGetReviewById() {
        // Given
        when(reviewRepository.findById("review-1")).thenReturn(Optional.of(testReview));

        // When
        Review result = reviewService.getById("review-1");

        // Then
        assertNotNull(result);
        assertEquals("review-1", result.getId());
        verify(reviewRepository).findById("review-1");
    }

    @Test
    void shouldThrowResourceNotFoundWhenReviewNotFound() {
        // Given
        when(reviewRepository.findById("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> reviewService.getById("nonexistent"));
        verify(reviewRepository).findById("nonexistent");
    }

    @Test
    void shouldGetAllReviews() {
        // Given
        List<Review> reviews = Arrays.asList(testReview, new Review());
        when(reviewRepository.findAll()).thenReturn(reviews);

        // When
        List<Review> result = reviewService.getAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reviewRepository).findAll();
    }

    @Test
    void shouldGetReviewsForSubject() {
        // Given
        List<Review> reviews = Arrays.asList(testReview);
        when(reviewRepository.findBySubjectIdAndSubjectType("dj-1", "DJ")).thenReturn(reviews);

        // When
        List<Review> result = reviewService.getReviewsForSubject("dj-1", "DJ");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reviewRepository).findBySubjectIdAndSubjectType("dj-1", "DJ");
    }

    @Test
    void shouldCalculateAverageRating() {
        // Given
        Review review1 = new Review("dj-1", "DJ", "User1", 4, "Good");
        Review review2 = new Review("dj-1", "DJ", "User2", 5, "Excellent");
        List<Review> reviews = Arrays.asList(review1, review2);
        when(reviewRepository.findBySubjectIdAndSubjectType("dj-1", "DJ")).thenReturn(reviews);

        // When
        Double result = reviewService.getAverageRating("dj-1", "DJ");

        // Then
        assertEquals(4.5, result);
        verify(reviewRepository).findBySubjectIdAndSubjectType("dj-1", "DJ");
    }

    @Test
    void shouldReturnZeroAverageWhenNoReviews() {
        // Given
        when(reviewRepository.findBySubjectIdAndSubjectType("dj-1", "DJ")).thenReturn(Arrays.asList());

        // When
        Double result = reviewService.getAverageRating("dj-1", "DJ");

        // Then
        assertEquals(0.0, result);
        verify(reviewRepository).findBySubjectIdAndSubjectType("dj-1", "DJ");
    }

    @Test
    void shouldDeleteReview() {
        // Given
        when(reviewRepository.existsById("review-1")).thenReturn(true);

        // When
        reviewService.delete("review-1");

        // Then
        verify(reviewRepository).existsById("review-1");
        verify(reviewRepository).deleteById("review-1");
    }

    @Test
    void shouldThrowResourceNotFoundWhenDeletingNonexistentReview() {
        // Given
        when(reviewRepository.existsById("nonexistent")).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> reviewService.delete("nonexistent"));
        verify(reviewRepository).existsById("nonexistent");
        verify(reviewRepository, never()).deleteById(any());
    }
}
