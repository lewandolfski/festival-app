package com.capgemini.festivalapplication.controller;

import com.capgemini.festivalapplication.dto.PerformanceDto;
import com.capgemini.festivalapplication.entity.Performance;
import com.capgemini.festivalapplication.service.PerformanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Performance management operations.
 * 
 * This controller provides full CRUD operations for Performance entities:
 * - Create new performances with DJ validation
 * - Read performance information (single or all)
 * - Update existing performances
 * - Delete performances
 * - Search performances by DJ
 * 
 * All operations use DTOs for data transfer and include proper validation.
 * Exception handling is managed by the GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/api/performances")
@CrossOrigin(origins = "*")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    /**
     * Creates a new performance.
     * @param performanceDto Performance data transfer object with validation
     * @return Created performance entity with HTTP 201 status
     * @throws BadRequestException if validation fails or DJ doesn't exist
     */
    @PostMapping
    public ResponseEntity<Performance> createPerformance(@Valid @RequestBody PerformanceDto performanceDto) {
        Performance savedPerformance = performanceService.create(performanceDto);
        return new ResponseEntity<>(savedPerformance, HttpStatus.CREATED);
    }

    /**
     * Retrieves all performances from the database.
     * @return List of all performance entities with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Performance>> getAllPerformances() {
        List<Performance> performances = performanceService.getAll();
        return ResponseEntity.ok(performances);
    }

    /**
     * Retrieves a specific performance by ID.
     * @param id Performance identifier
     * @return Performance entity with HTTP 200 status
     * @throws ResourceNotFoundException if performance not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Performance> getPerformanceById(@PathVariable String id) {
        Performance performance = performanceService.getById(id);
        return ResponseEntity.ok(performance);
    }

    /**
     * Updates an existing performance.
     * @param id Performance identifier
     * @param performanceDto Updated performance data
     * @return Updated performance entity with HTTP 200 status
     * @throws BadRequestException if performance doesn't exist or validation fails
     */
    @PutMapping("/{id}")
    public ResponseEntity<Performance> updatePerformance(@PathVariable String id, @Valid @RequestBody PerformanceDto performanceDto) {
        Performance updatedPerformance = performanceService.update(id, performanceDto);
        return ResponseEntity.ok(updatedPerformance);
    }

    /**
     * Deletes a performance by ID.
     * @param id Performance identifier
     * @return HTTP 204 No Content status
     * @throws ResourceNotFoundException if performance not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformance(@PathVariable String id) {
        performanceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all performances by a specific DJ.
     * @param djId DJ identifier
     * @return List of performances by the specified DJ
     */
    @GetMapping("/dj/{djId}")
    public ResponseEntity<List<Performance>> getPerformancesByDjId(@PathVariable String djId) {
        List<Performance> performances = performanceService.getByDjId(djId);
        return ResponseEntity.ok(performances);
    }
}
