package com.example.examen.service;

import com.example.examen.model.entity.Matiere;
import java.util.List;
import java.util.Optional;

public interface MatiereService {
    List<Matiere> findAll();
    Optional<Matiere> findById(Long id);
    Matiere save(Matiere matiere);
    Matiere update(Long id, Matiere matiere);
    void deleteById(Long id);
}
