package com.example.examen.service.impl;

import com.example.examen.model.entity.*;
import com.example.examen.repository.*;
import com.example.examen.service.NoteFinaleServiceInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class NoteFinaleServiceImpl implements NoteFinaleServiceInterface {

    private final NoteRepository noteRepository;
    private final ParametreRepository parametreRepository;
    private final NoteFinaleRepository noteFinaleRepository;
    private final CandidatRepository candidatRepository;
    private final MatiereRepository matiereRepository;

    public NoteFinaleServiceImpl(NoteRepository noteRepository,
                                 ParametreRepository parametreRepository,
                                 NoteFinaleRepository noteFinaleRepository,
                                 CandidatRepository candidatRepository,
                                 MatiereRepository matiereRepository) {
        this.noteRepository = noteRepository;
        this.parametreRepository = parametreRepository;
        this.noteFinaleRepository = noteFinaleRepository;
        this.candidatRepository = candidatRepository;
        this.matiereRepository = matiereRepository;
    }

    @Override
    public List<NoteFinale> findAll() {
        return noteFinaleRepository.findAll();
    }

    @Override
    public Optional<NoteFinale> findById(Long id) {
        return noteFinaleRepository.findById(id);
    }

    @Override
    public Optional<NoteFinale> findByCandidatAndMatiere(Long idCandidat, Long idMatiere) {
        return noteFinaleRepository.findByCandidatIdAndMatiereId(idCandidat, idMatiere);
    }

    @Override
    public void deleteById(Long id) {
        noteFinaleRepository.deleteById(id);
    }

    /**
     * Calcule et enregistre la note finale d'un candidat pour une matière.
     *
     * Algorithme :
     * 1. Récupérer toutes les notes du candidat pour la matière
     * 2. Calculer max, min, et différence = max - min
     * 3. Parcourir les paramètres de la matière (dans l'ordre)
     * 4. Pour chaque paramètre, évaluer : différence <opérateur> diff
     *    - opérateur 1 = "<"  (strictement inférieur)
     *    - opérateur 2 = ">"  (strictement supérieur)
     * 5. Si la condition est vraie, appliquer la résolution :
     *    - "plus_petit" → note min
     *    - "plus_grand" → note max
     *    - "moyenne"    → moyenne de toutes les notes
     * 6. Enregistrer le résultat dans t_notefinale
     */
    @Override
    @Transactional
    public NoteFinale calculerNoteFinale(Long idCandidat, Long idMatiere) {

        Candidat candidat = candidatRepository.findById(idCandidat)
                .orElseThrow(() -> new RuntimeException("Candidat introuvable : id=" + idCandidat));

        Matiere matiere = matiereRepository.findById(idMatiere)
                .orElseThrow(() -> new RuntimeException("Matière introuvable : id=" + idMatiere));

        // 1. Récupérer les notes
        List<Note> notes = noteRepository.findByCandidatIdAndMatiereId(idCandidat, idMatiere);

        if (notes.isEmpty()) {
            throw new RuntimeException("Aucune note trouvée pour le candidat " + idCandidat
                    + " et la matière " + idMatiere);
        }

        // 2. Calculer max, min, différence
        BigDecimal noteMax = notes.stream()
                .map(Note::getNote)
                .max(BigDecimal::compareTo)
                .orElseThrow();

        BigDecimal noteMin = notes.stream()
                .map(Note::getNote)
                .min(BigDecimal::compareTo)
                .orElseThrow();

        BigDecimal difference = noteMax.subtract(noteMin);

        // 3. Récupérer les paramètres de la matière
        List<Parametre> parametres = parametreRepository.findByMatiereIdOrderByIdAsc(idMatiere);

        if (parametres.isEmpty()) {
            throw new RuntimeException("Aucun paramètre configuré pour la matière " + idMatiere);
        }

        // 4 & 5. Évaluer chaque paramètre et appliquer la résolution
        BigDecimal noteFinaleValue = null;

        for (Parametre parametre : parametres) {
            int operateurCode = parametre.getOperateur().getOperateur();
            BigDecimal diffParam = parametre.getDiff();
            String resolutionNom = parametre.getResolution().getNom();

            boolean conditionSatisfaite = false;

            if (operateurCode == 1) {
                // opérateur "<" : différence < diff
                conditionSatisfaite = difference.compareTo(diffParam) < 0;
            } else if (operateurCode == 2) {
                // opérateur ">" : différence > diff
                conditionSatisfaite = difference.compareTo(diffParam) > 0;
            }

            if (conditionSatisfaite) {
                noteFinaleValue = appliquerResolution(resolutionNom, notes, noteMin, noteMax);
                break;
            }
        }

        // Si aucune règle n'a matché, on prend la moyenne par défaut
        if (noteFinaleValue == null) {
            noteFinaleValue = calculerMoyenne(notes);
        }

        // 6. Enregistrer dans t_notefinale
        NoteFinale noteFinale = noteFinaleRepository
                .findByCandidatIdAndMatiereId(idCandidat, idMatiere)
                .orElse(new NoteFinale());

        noteFinale.setCandidat(candidat);
        noteFinale.setMatiere(matiere);
        noteFinale.setNoteFinale(noteFinaleValue);

        return noteFinaleRepository.save(noteFinale);
    }

    private BigDecimal appliquerResolution(String resolution,
                                           List<Note> notes,
                                           BigDecimal noteMin,
                                           BigDecimal noteMax) {
        return switch (resolution) {
            case "plus_petit" -> noteMin;
            case "plus_grand" -> noteMax;
            case "moyenne" -> calculerMoyenne(notes);
            default -> throw new RuntimeException("Résolution inconnue : " + resolution);
        };
    }

    private BigDecimal calculerMoyenne(List<Note> notes) {
        BigDecimal somme = notes.stream()
                .map(Note::getNote)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return somme.divide(BigDecimal.valueOf(notes.size()), 2, RoundingMode.HALF_UP);
    }
}
