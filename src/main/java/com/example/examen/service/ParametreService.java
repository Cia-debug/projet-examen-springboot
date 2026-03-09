package com.example.examen.service;

import com.example.examen.model.entity.Parametre;
import java.util.List;
import java.util.Optional;

public interface ParametreService {
    List<Parametre> findAll();
    Optional<Parametre> findById(Long id);
    List<Parametre> findByMatiereId(Long matiereId);
    Parametre save(Parametre parametre);
    Parametre update(Long id, Parametre parametre);
    void deleteById(Long id);
}
