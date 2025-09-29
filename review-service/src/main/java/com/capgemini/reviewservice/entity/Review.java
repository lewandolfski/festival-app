package com.capgemini.reviewservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Review entity for MongoDB storage.
 * Represents a review for either a DJ or a Performance from the Festival Application.
 */
@Document(collection = "reviews")
public class Review {

    @Id
    private String id;

    @NotBlank(message = "Subject ID is required")
    @Field("subject_id")
    private String subjectId; // DJ ID or Performance ID from Festival Application

    @NotBlank(message = "Subject type is required")
    @Pattern(regexp = "DJ|PERFORMANCE", message = "Subject type must be either DJ or PERFORMANCE")
    @Field("subject_type")
    private String subjectType; // "DJ" or "PERFORMANCE"

    @NotBlank(message = "Reviewer name is required")
    @Size(min = 2, max = 100, message = "Reviewer name must be between 2 and 100 characters")
    @Field("reviewer_name")
    private String reviewerName;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String comment;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Review() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Review(String subjectId, String subjectType, String reviewerName, Integer rating, String comment) {
        this();
        this.subjectId = subjectId;
        this.subjectType = subjectType;
        this.reviewerName = reviewerName;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Update timestamp when review is modified
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", subjectType='" + subjectType + '\'' +
                ", reviewerName='" + reviewerName + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
