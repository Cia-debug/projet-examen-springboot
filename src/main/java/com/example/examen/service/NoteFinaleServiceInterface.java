package com.example.examen.service;

import com.example.examen.model.entity.NoteFinale;
import java.util.List;
import java.util.Optional;

public interface NoteFinaleServiceInterface {
    List<NoteFinale> findAll();
    Optional<NoteFinale> findById(Long id);
    Optional<NoteFinale> findByCandidatAndMatiere(Long idCandidat, Long idMatiere);
    NoteFinale calculerNoteFinale(Long idCandidat, Long idMatiere);
    void deleteById(Long id);
}
