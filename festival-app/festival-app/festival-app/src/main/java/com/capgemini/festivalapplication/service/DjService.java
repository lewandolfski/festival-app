package com.capgemini.festivalapplication.service;

import com.capgemini.festivalapplication.dto.DjDto;
import com.capgemini.festivalapplication.entity.Dj;

import java.util.List;

public interface DjService {
    Dj create(DjDto dto);
    Dj update(String id, DjDto dto);
    void delete(String id);
    Dj getById(String id);
    List<Dj> getAll();
    List<Dj> getByGenre(String genre);
    List<Dj> getByName(String name);
    List<Dj> getDjsWithLongNames();
}
