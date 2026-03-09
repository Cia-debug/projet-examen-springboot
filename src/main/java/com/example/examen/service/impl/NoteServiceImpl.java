package com.example.examen.service.impl;

import com.example.examen.model.entity.Note;
import com.example.examen.model.entity.Candidat;
import com.example.examen.model.entity.Correcteur;
import com.example.examen.model.entity.Matiere;
import com.example.examen.repository.NoteRepository;
import com.example.examen.repository.CandidatRepository;
import com.example.examen.repository.CorrecteurRepository;
import com.example.examen.repository.MatiereRepository;
import com.example.examen.service.NoteService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final CandidatRepository candidatRepository;
    private final CorrecteurRepository correcteurRepository;
    private final MatiereRepository matiereRepository;

    public NoteServiceImpl(NoteRepository noteRepository,
                           CandidatRepository candidatRepository,
                           CorrecteurRepository correcteurRepository,
                           MatiereRepository matiereRepository) {
        this.noteRepository = noteRepository;
        this.candidatRepository = candidatRepository;
        this.correcteurRepository = correcteurRepository;
        this.matiereRepository = matiereRepository;
    }

    @Override
    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    @Override
    public Optional<Note> findById(Long id) {
        return noteRepository.findById(id);
    }

    @Override
    public List<Note> findByCandidatAndMatiere(Long idCandidat, Long idMatiere) {
        return noteRepository.findByCandidatIdAndMatiereId(idCandidat, idMatiere);
    }

    @Override
    public Note save(Long idCandidat, Long idCorrecteur, Long idMatiere, BigDecimal noteValue) {
        Candidat candidat = candidatRepository.findById(idCandidat)
                .orElseThrow(() -> new RuntimeException("Candidat introuvable : id=" + idCandidat));
        Correcteur correcteur = correcteurRepository.findById(idCorrecteur)
                .orElseThrow(() -> new RuntimeException("Correcteur introuvable : id=" + idCorrecteur));
        Matiere matiere = matiereRepository.findById(idMatiere)
                .orElseThrow(() -> new RuntimeException("Matière introuvable : id=" + idMatiere));

        Note note = new Note(candidat, correcteur, matiere, noteValue);
        return noteRepository.save(note);
    }

    @Override
    public Note update(Long id, Long idCandidat, Long idCorrecteur, Long idMatiere, BigDecimal noteValue) {
        Note existing = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note introuvable : id=" + id));

        Candidat candidat = candidatRepository.findById(idCandidat)
                .orElseThrow(() -> new RuntimeException("Candidat introuvable : id=" + idCandidat));
        Correcteur correcteur = correcteurRepository.findById(idCorrecteur)
                .orElseThrow(() -> new RuntimeException("Correcteur introuvable : id=" + idCorrecteur));
        Matiere matiere = matiereRepository.findById(idMatiere)
                .orElseThrow(() -> new RuntimeException("Matière introuvable : id=" + idMatiere));

        existing.setCandidat(candidat);
        existing.setCorrecteur(correcteur);
        existing.setMatiere(matiere);
        existing.setNote(noteValue);
        return noteRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        noteRepository.deleteById(id);
    }
}
