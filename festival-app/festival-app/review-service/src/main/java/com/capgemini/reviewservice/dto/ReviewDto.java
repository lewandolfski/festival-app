package com.capgemini.reviewservice.dto;

import jakarta.validation.constraints.*;

/**
 * Data Transfer Object for Review operations.
 * Used for API requests and responses.
 */
public class ReviewDto {

    @NotBlank(message = "Subject ID is required")
    private String subjectId;

    @NotBlank(message = "Subject type is required")
    @Pattern(regexp = "DJ|PERFORMANCE", message = "Subject type must be either DJ or PERFORMANCE")
    private String subjectType;

    @NotBlank(message = "Reviewer name is required")
    @Size(min = 2, max = 100, message = "Reviewer name must be between 2 and 100 characters")
    private String reviewerName;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String comment;

    // Constructors
    public ReviewDto() {}

    public ReviewDto(String subjectId, String subjectType, String reviewerName, Integer rating, String comment) {
        this.subjectId = subjectId;
        this.subjectType = subjectType;
        this.reviewerName = reviewerName;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "ReviewDto{" +
                "subjectId='" + subjectId + '\'' +
                ", subjectType='" + subjectType + '\'' +
                ", reviewerName='" + reviewerName + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}
