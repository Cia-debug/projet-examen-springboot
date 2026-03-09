package com.example.examen.service;

import com.example.examen.model.entity.Note;
import java.util.List;
import java.util.Optional;

public interface NoteService {
    List<Note> findAll();
    Optional<Note> findById(Long id);
    List<Note> findByCandidatAndMatiere(Long idCandidat, Long idMatiere);
    Note save(Long idCandidat, Long idCorrecteur, Long idMatiere, java.math.BigDecimal noteValue);
    Note update(Long id, Long idCandidat, Long idCorrecteur, Long idMatiere, java.math.BigDecimal noteValue);
    void deleteById(Long id);
}
