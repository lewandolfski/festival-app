package com.capgemini.festivalapplication.service;

import com.capgemini.festivalapplication.dto.PerformanceDto;
import com.capgemini.festivalapplication.entity.Performance;

import java.util.List;

public interface PerformanceService {
    Performance create(PerformanceDto dto);
    Performance update(String id, PerformanceDto dto);
    void delete(String id);
    Performance getById(String id);
    List<Performance> getAll();
    List<Performance> getByDjId(String djId);
}
