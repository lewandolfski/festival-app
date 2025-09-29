package com.capgemini.festivalapplication.service;

import com.capgemini.festivalapplication.dto.PerformanceDto;
import com.capgemini.festivalapplication.entity.Performance;
import com.capgemini.festivalapplication.exception.BadRequestException;
import com.capgemini.festivalapplication.exception.ResourceNotFoundException;
import com.capgemini.festivalapplication.mapper.PerformanceMapper;
import com.capgemini.festivalapplication.repository.PerformanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for Performance business logic.
 * 
 * This service handles all Performance-related business operations including:
 * - CRUD operations with proper validation
 * - DTO to Entity conversion using PerformanceMapper
 * - DJ relationship validation
 * - Performance timing validation
 * - Exception handling for various error scenarios
 */
@Service
public class PerformanceServiceImpl implements PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final PerformanceMapper performanceMapper;

    public PerformanceServiceImpl(PerformanceRepository performanceRepository, PerformanceMapper performanceMapper) {
        this.performanceRepository = performanceRepository;
        this.performanceMapper = performanceMapper;
    }

    @Override
    public Performance create(PerformanceDto performanceDto) {
        // Convert DTO to Entity using mapper (includes DJ validation and timing validation)
        Performance performance = performanceMapper.toEntity(performanceDto);
        
        // Save and return the persisted entity
        return performanceRepository.save(performance);
    }

    @Override
    public Performance update(String id, PerformanceDto performanceDto) {
        // Check if Performance exists
        Performance existingPerformance = performanceRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(
                "Cannot update non-existing Performance with id " + id + ". " +
                "Update operations require an existing record."
            ));
        
        // Update entity using mapper (includes DJ validation and timing validation)
        Performance updatedPerformance = performanceMapper.updateEntity(existingPerformance, performanceDto);
        
        // Save and return the updated entity
        return performanceRepository.save(updatedPerformance);
    }

    @Override
    public void delete(String id) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Performance with id " + id + " not found. Cannot delete non-existing performance."
                ));
        performanceRepository.delete(performance);
    }

    @Override
    public Performance getById(String id) {
        return performanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Performance with id " + id + " not found."
                ));
    }

    @Override
    public List<Performance> getAll() {
        return performanceRepository.findAll();
    }

    @Override
    public List<Performance> getByDjId(String djId) {
        List<Performance> performances = performanceRepository.findByDj_Id(djId);
        if (performances.isEmpty()) {
            throw new ResourceNotFoundException(
                "No performances found for DJ with id: " + djId
            );
        }
        return performances;
    }
}