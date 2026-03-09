package com.example.examen.service;

import com.example.examen.model.entity.Resolution;
import java.util.List;
import java.util.Optional;

public interface ResolutionService {
    List<Resolution> findAll();
    Optional<Resolution> findById(Long id);
    Resolution save(Resolution resolution);
    Resolution update(Long id, Resolution resolution);
    void deleteById(Long id);
}
