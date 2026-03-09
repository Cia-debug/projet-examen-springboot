package com.example.examen.service;

import com.example.examen.model.entity.Correcteur;
import java.util.List;
import java.util.Optional;

public interface CorrecteurService {
    List<Correcteur> findAll();
    Optional<Correcteur> findById(Long id);
    Correcteur save(Correcteur correcteur);
    Correcteur update(Long id, Correcteur correcteur);
    void deleteById(Long id);
}
