package com.capgemini.festivalapplication.mapper;

import com.capgemini.festivalapplication.dto.PerformanceDto;
import com.capgemini.festivalapplication.entity.Dj;
import com.capgemini.festivalapplication.entity.Performance;
import com.capgemini.festivalapplication.exception.BadRequestException;
import com.capgemini.festivalapplication.repository.DjRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between Performance DTOs and Entities.
 * 
 * This mapper handles the conversion between:
 * - PerformanceDto (Data Transfer Object) used in API requests/responses
 * - Performance (Entity) used for database persistence
 * 
 * The mapper also handles DJ relationship resolution, ensuring that
 * performances are properly linked to existing DJs.
 */
@Component
public class PerformanceMapper {

    @Autowired
    private DjRepository djRepository;

    /**
     * Converts a PerformanceDto to a Performance entity.
     * Used when creating or updating Performance records from API requests.
     * 
     * This method also resolves the DJ relationship by looking up the DJ
     * entity using the djId from the DTO.
     * 
     * @param performanceDto The DTO containing Performance data from API request
     * @return Performance entity ready for database operations
     * @throws BadRequestException if the referenced DJ doesn't exist
     */
    public Performance toEntity(PerformanceDto performanceDto) {
        if (performanceDto == null) {
            return null;
        }

        // Validate that DJ exists
        Dj dj = djRepository.findById(performanceDto.getDjId())
            .orElseThrow(() -> new BadRequestException(
                "DJ with ID " + performanceDto.getDjId() + " not found. " +
                "Cannot create performance for non-existing DJ."
            ));

        Performance performance = new Performance();
        // Don't set ID for new entities - let the entity generate it
        // Only set ID if it's provided (for updates)
        if (performanceDto.getId() != null && !performanceDto.getId().isEmpty()) {
            performance.setId(performanceDto.getId());
        }
        performance.setTitle(performanceDto.getTitle());
        performance.setDescription(performanceDto.getDescription());
        performance.setStartTime(performanceDto.getStartTime());
        performance.setEndTime(performanceDto.getEndTime());
        performance.setDj(dj);
        
        // Validate performance duration
        validatePerformanceTiming(performance);
        
        return performance;
    }

    /**
     * Converts a Performance entity to a PerformanceDto.
     * Used when returning Performance data in API responses.
     * 
     * @param performance The entity from database
     * @return PerformanceDto for API response
     */
    public PerformanceDto toDto(Performance performance) {
        if (performance == null) {
            return null;
        }

        PerformanceDto performanceDto = new PerformanceDto();
        performanceDto.setId(performance.getId());
        performanceDto.setTitle(performance.getTitle());
        performanceDto.setDescription(performance.getDescription());
        performanceDto.setStartTime(performance.getStartTime());
        performanceDto.setEndTime(performance.getEndTime());
        
        // Extract DJ ID from the relationship
        if (performance.getDj() != null) {
            performanceDto.setDjId(performance.getDj().getId());
        }
        
        return performanceDto;
    }

    /**
     * Updates an existing Performance entity with data from a PerformanceDto.
     * Used for PUT operations where we want to update an existing entity
     * while preserving certain fields (like ID, creation date, etc.).
     * 
     * @param existingPerformance The existing entity from database
     * @param performanceDto The DTO containing updated data
     * @return The updated entity ready for persistence
     * @throws BadRequestException if the referenced DJ doesn't exist
     */
    public Performance updateEntity(Performance existingPerformance, PerformanceDto performanceDto) {
        if (existingPerformance == null || performanceDto == null) {
            return existingPerformance;
        }

        // Validate that DJ exists if djId is being updated
        if (performanceDto.getDjId() != null && 
            !performanceDto.getDjId().equals(existingPerformance.getDj().getId())) {
            
            Dj dj = djRepository.findById(performanceDto.getDjId())
                .orElseThrow(() -> new BadRequestException(
                    "DJ with ID " + performanceDto.getDjId() + " not found. " +
                    "Cannot update performance with non-existing DJ."
                ));
            existingPerformance.setDj(dj);
        }

        // Update the fields that can be modified
        existingPerformance.setTitle(performanceDto.getTitle());
        existingPerformance.setDescription(performanceDto.getDescription());
        existingPerformance.setStartTime(performanceDto.getStartTime());
        existingPerformance.setEndTime(performanceDto.getEndTime());
        
        // Validate performance duration
        validatePerformanceTiming(existingPerformance);
        
        return existingPerformance;
    }

    /**
     * Validates that the performance timing is logical.
     * Ensures that start time is before end time.
     * 
     * @param performance The performance to validate
     * @throws BadRequestException if timing is invalid
     */
    private void validatePerformanceTiming(Performance performance) {
        if (performance.getStartTime() != null && performance.getEndTime() != null) {
            if (performance.getStartTime().isAfter(performance.getEndTime())) {
                throw new BadRequestException(
                    "Performance start time cannot be after end time. " +
                    "Start: " + performance.getStartTime() + ", End: " + performance.getEndTime()
                );
            }
            
            if (performance.getStartTime().isEqual(performance.getEndTime())) {
                throw new BadRequestException(
                    "Performance start time cannot be equal to end time. " +
                    "Performance must have a duration."
                );
            }
        }
    }
}