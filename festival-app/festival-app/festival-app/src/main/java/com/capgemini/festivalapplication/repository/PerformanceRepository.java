package com.capgemini.festivalapplication.repository;

import com.capgemini.festivalapplication.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, String> {
    
    List<Performance> findByDj_Id(String djId);
    
    List<Performance> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
