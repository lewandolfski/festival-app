package com.capgemini.festivalapplication.controller;

import com.capgemini.festivalapplication.dto.DjDto;
import com.capgemini.festivalapplication.entity.Dj;
import com.capgemini.festivalapplication.service.DjService;
import com.capgemini.festivalapplication.service.ReviewServiceClient;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for DJ management operations.
 * 
 * This controller provides full CRUD operations for DJ entities:
 * - Create new DJs with validation
 * - Read DJ information (single or all)
 * - Update existing DJs (prevents creating new records)
 * - Delete DJs
 * - Search DJs by various criteria
 * 
 * All operations use DTOs for data transfer and include proper validation.
 * Exception handling is managed by the GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/api/djs")
@CrossOrigin(origins = "*") // Allow CORS for frontend integration
public class DjController {

    // Using constructor injection for better testability and immutability
    private final DjService djService;
    private final ReviewServiceClient reviewServiceClient;

    public DjController(DjService djService, ReviewServiceClient reviewServiceClient) {
        this.djService = djService;
        this.reviewServiceClient = reviewServiceClient;
    }

    /**
     * Creates a new DJ.
     * @param djDto DJ data transfer object with validation
     * @return Created DJ entity with HTTP 201 status
     * @throws BadRequestException if validation fails
     */
    @PostMapping
    public ResponseEntity<Dj> createDj(@Valid @RequestBody DjDto djDto) {
        Dj savedDj = djService.create(djDto);
        return new ResponseEntity<>(savedDj, HttpStatus.CREATED);
    }

    /**
     * Retrieves all DJs from the database.
     * @return List of all DJ entities with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Dj>> getAllDjs() {
        List<Dj> djs = djService.getAll();
        return ResponseEntity.ok(djs);
    }

    /**
     * Retrieves a specific DJ by ID.
     * @param id DJ identifier
     * @return DJ entity with HTTP 200 status
     * @throws ResourceNotFoundException if DJ not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Dj> getDjById(@PathVariable String id) {
        Dj dj = djService.getById(id);
        return ResponseEntity.ok(dj);
    }

    /**
     * Updates an existing DJ. Will NOT create a new record if ID doesn't exist.
     * @param id DJ identifier
     * @param djDto Updated DJ data
     * @return Updated DJ entity with HTTP 200 status
     * @throws BadRequestException if DJ doesn't exist or validation fails
     */
    @PutMapping("/{id}")
    public ResponseEntity<Dj> updateDj(@PathVariable String id, @Valid @RequestBody DjDto djDto) {
        Dj updatedDj = djService.update(id, djDto);
        return ResponseEntity.ok(updatedDj);
    }

    /**
     * Deletes a DJ by ID.
     * @param id DJ identifier
     * @return HTTP 204 No Content status
     * @throws ResourceNotFoundException if DJ not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDj(@PathVariable String id) {
        djService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Searches DJs by genre.
     * @param genre Genre to search for
     * @return List of DJs matching the genre
     */
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Dj>> getDjsByGenre(@PathVariable String genre) {
        List<Dj> djs = djService.getByGenre(genre);
        return ResponseEntity.ok(djs);
    }
    
    /**
     * Searches DJs by name.
     * @param name DJ name to search for
     * @return List of DJs matching the name
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<List<Dj>> getDjsByName(@PathVariable String name) {
        List<Dj> djs = djService.getByName(name);
        return ResponseEntity.ok(djs);
    }
    
    /**
     * Retrieves DJs with names longer than 6 characters.
     * @return List of DJs with long names
     */
    @GetMapping("/long-names")
    public ResponseEntity<List<Dj>> getDjsWithLongNames() {
        List<Dj> djs = djService.getDjsWithLongNames();
        return ResponseEntity.ok(djs);
    }

    /**
     * Get all reviews for a specific DJ.
     * @param id DJ identifier
     * @return JSON string containing reviews
     */
    @GetMapping("/{id}/reviews")
    public ResponseEntity<String> getDjReviews(@PathVariable String id) {
        // Verify DJ exists first
        djService.getById(id);
        
        String reviews = reviewServiceClient.getReviewsForDJ(Long.parseLong(id));
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get average rating for a specific DJ.
     * @param id DJ identifier
     * @return Average rating as Double
     */
    @GetMapping("/{id}/rating")
    public ResponseEntity<Double> getDjAverageRating(@PathVariable String id) {
        // Verify DJ exists first
        djService.getById(id);
        
        Double averageRating = reviewServiceClient.getAverageRatingForDJ(Long.parseLong(id));
        return ResponseEntity.ok(averageRating);
    }

    /**
     * Get review count for a specific DJ.
     * @param id DJ identifier
     * @return Number of reviews
     */
    @GetMapping("/{id}/review-count")
    public ResponseEntity<Long> getDjReviewCount(@PathVariable String id) {
        // Verify DJ exists first
        djService.getById(id);
        
        Long reviewCount = reviewServiceClient.getReviewCountForDJ(Long.parseLong(id));
        return ResponseEntity.ok(reviewCount);
    }
}
