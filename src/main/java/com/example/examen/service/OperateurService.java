package com.example.examen.service;

import com.example.examen.model.entity.Operateur;
import java.util.List;
import java.util.Optional;

public interface OperateurService {
    List<Operateur> findAll();
    Optional<Operateur> findById(Long id);
    Operateur save(Operateur operateur);
    Operateur update(Long id, Operateur operateur);
    void deleteById(Long id);
}
