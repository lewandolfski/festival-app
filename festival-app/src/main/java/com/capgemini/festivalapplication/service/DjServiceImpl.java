package com.capgemini.festivalapplication.service;

import com.capgemini.festivalapplication.dto.DjDto;
import com.capgemini.festivalapplication.entity.Dj;
import com.capgemini.festivalapplication.exception.BadRequestException;
import com.capgemini.festivalapplication.exception.ResourceNotFoundException;
import com.capgemini.festivalapplication.mapper.DjMapper;
import com.capgemini.festivalapplication.repository.DjRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for DJ business logic.
 * 
 * This service handles all DJ-related business operations including:
 * - CRUD operations with proper validation
 * - DTO to Entity conversion using DjMapper
 * - Business rule enforcement
 * - Exception handling for various error scenarios
 */
@Service
public class DjServiceImpl implements DjService {

    private final DjRepository djRepository;
    private final DjMapper djMapper;

    public DjServiceImpl(DjRepository djRepository, DjMapper djMapper) {
        this.djRepository = djRepository;
        this.djMapper = djMapper;
    }

    @Override
    public Dj create(DjDto djDto) {
        // Check if DJ with same email already exists
        if (djRepository.findByEmail(djDto.getEmail()).isPresent()) {
            throw new BadRequestException(
                "DJ with email " + djDto.getEmail() + " already exists. " +
                "Email addresses must be unique."
            );
        }
        
        // Convert DTO to Entity using mapper
        Dj dj = djMapper.toEntity(djDto);
        
        // Save and return the persisted entity
        return djRepository.save(dj);
    }

    @Override
    public Dj update(String id, DjDto djDto) {
        // Check if DJ exists
        Dj existingDj = djRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(
                "Cannot update non-existing DJ with id " + id + ". " +
                "Update operations require an existing record."
            ));
        
        // Check if email is being changed to an already existing email
        if (!existingDj.getEmail().equals(djDto.getEmail()) && 
            djRepository.findByEmail(djDto.getEmail()).isPresent()) {
            throw new BadRequestException(
                "DJ with email " + djDto.getEmail() + " already exists. " +
                "Email addresses must be unique."
            );
        }
        
        // Update entity using mapper
        Dj updatedDj = djMapper.updateEntity(existingDj, djDto);
        
        // Save and return the updated entity
        return djRepository.save(updatedDj);
    }

    @Override
    public void delete(String id) {
        Dj existing = djRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "DJ with id " + id + " not found. Cannot delete non-existing DJ."
                ));
        djRepository.delete(existing);
    }

    @Override
    public Dj getById(String id) {
        return djRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "DJ with id " + id + " not found."
                ));
    }

    @Override
    public List<Dj> getAll() {
        return djRepository.findAll();
    }

    @Override
    public List<Dj> getByGenre(String genre) {
        List<Dj> djs = djRepository.findByGenre(genre);
        if (djs.isEmpty()) {
            throw new ResourceNotFoundException(
                "No DJs found for genre: " + genre
            );
        }
        return djs;
    }

    @Override
    public List<Dj> getByName(String name) {
        List<Dj> djs = djRepository.findByName(name);
        if (djs.isEmpty()) {
            throw new ResourceNotFoundException(
                "No DJs found with name: " + name
            );
        }
        return djs;
    }

    @Override
    public List<Dj> getDjsWithLongNames() {
        return djRepository.findDjsWithNameLongerThan6Characters();
    }
}