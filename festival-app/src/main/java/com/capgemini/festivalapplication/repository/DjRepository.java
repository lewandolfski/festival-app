package com.capgemini.festivalapplication.repository;

import com.capgemini.festivalapplication.entity.Dj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DjRepository extends JpaRepository<Dj, String> {
    
    Optional<Dj> findByEmail(String email);
    
    List<Dj> findByGenre(String genre);
    
    // Exercise 2: Find DJs by name
    List<Dj> findByName(String name);
    
    // Exercise 2: Find DJs with names longer than 6 characters
    @Query("SELECT d FROM Dj d WHERE LENGTH(d.name) > 6")
    List<Dj> findDjsWithNameLongerThan6Characters();
}
