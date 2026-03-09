package com.example.examen.service;

import com.example.examen.model.entity.Candidat;
import java.util.List;
import java.util.Optional;

public interface CandidatService {
    List<Candidat> findAll();
    Optional<Candidat> findById(Long id);
    Candidat save(Candidat candidat);
    Candidat update(Long id, Candidat candidat);
    void deleteById(Long id);
}
