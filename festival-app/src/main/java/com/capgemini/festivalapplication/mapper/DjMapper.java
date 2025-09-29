package com.capgemini.festivalapplication.mapper;

import com.capgemini.festivalapplication.dto.DjDto;
import com.capgemini.festivalapplication.entity.Dj;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between DJ DTOs and Entities.
 * 
 * This mapper handles the conversion between:
 * - DjDto (Data Transfer Object) used in API requests/responses
 * - Dj (Entity) used for database persistence
 * 
 * The mapper ensures proper data transformation while maintaining
 * separation between API contracts and domain models.
 */
@Component
public class DjMapper {

    /**
     * Converts a DjDto to a Dj entity.
     * Used when creating or updating DJ records from API requests.
     * 
     * @param djDto The DTO containing DJ data from API request
     * @return Dj entity ready for database operations
     */
    public Dj toEntity(DjDto djDto) {
        if (djDto == null) {
            return null;
        }

        Dj dj = new Dj();
        // Don't set ID for new entities - let the entity generate it
        // Only set ID if it's provided (for updates)
        if (djDto.getId() != null && !djDto.getId().isEmpty()) {
            dj.setId(djDto.getId());
        }
        dj.setName(djDto.getName());
        dj.setGenre(djDto.getGenre());
        dj.setEmail(djDto.getEmail());
        
        return dj;
    }

    /**
     * Converts a Dj entity to a DjDto.
     * Used when returning DJ data in API responses.
     * 
     * @param dj The entity from database
     * @return DjDto for API response
     */
    public DjDto toDto(Dj dj) {
        if (dj == null) {
            return null;
        }

        DjDto djDto = new DjDto();
        djDto.setId(dj.getId());
        djDto.setName(dj.getName());
        djDto.setGenre(dj.getGenre());
        djDto.setEmail(dj.getEmail());
        
        return djDto;
    }

    /**
     * Updates an existing Dj entity with data from a DjDto.
     * Used for PUT operations where we want to update an existing entity
     * while preserving certain fields (like ID, creation date, etc.).
     * 
     * @param existingDj The existing entity from database
     * @param djDto The DTO containing updated data
     * @return The updated entity ready for persistence
     */
    public Dj updateEntity(Dj existingDj, DjDto djDto) {
        if (existingDj == null || djDto == null) {
            return existingDj;
        }

        // Update only the fields that can be modified
        // ID should not be updated to maintain referential integrity
        existingDj.setName(djDto.getName());
        existingDj.setGenre(djDto.getGenre());
        existingDj.setEmail(djDto.getEmail());
        
        return existingDj;
    }
}