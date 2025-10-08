package com.capgemini.reviewservice.mapper;

import com.capgemini.reviewservice.dto.ReviewDto;
import com.capgemini.reviewservice.entity.Review;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Review entities and DTOs.
 * Handles the conversion logic between different representations.
 */
@Component
public class ReviewMapper {

    /**
     * Convert ReviewDto to Review entity.
     *
     * @param dto the ReviewDto to convert
     * @return the Review entity
     */
    public Review toEntity(ReviewDto dto) {
        if (dto == null) {
            return null;
        }

        return new Review(
                dto.getSubjectId(),
                dto.getSubjectType(),
                dto.getReviewerName(),
                dto.getRating(),
                dto.getComment()
        );
    }

    /**
     * Convert Review entity to ReviewDto.
     *
     * @param entity the Review entity to convert
     * @return the ReviewDto
     */
    public ReviewDto toDto(Review entity) {
        if (entity == null) {
            return null;
        }

        return new ReviewDto(
                entity.getSubjectId(),
                entity.getSubjectType(),
                entity.getReviewerName(),
                entity.getRating(),
                entity.getComment()
        );
    }

    /**
     * Update an existing Review entity with data from ReviewDto.
     *
     * @param dto the ReviewDto with updated data
     * @param entity the Review entity to update
     */
    public void updateEntityFromDto(ReviewDto dto, Review entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setSubjectId(dto.getSubjectId());
        entity.setSubjectType(dto.getSubjectType());
        entity.setReviewerName(dto.getReviewerName());
        entity.setRating(dto.getRating());
        entity.setComment(dto.getComment());
    }
}
