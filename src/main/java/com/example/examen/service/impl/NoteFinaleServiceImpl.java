package com.example.examen.service.impl;

import com.example.examen.model.entity.*;
import com.example.examen.repository.*;
import com.example.examen.service.NoteFinaleServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class NoteFinaleServiceImpl implements NoteFinaleServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(NoteFinaleServiceImpl.class);

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

        logger.info("Calcul note finale pour candidat: {}, matiere: {}. Difference: {}", idCandidat, idMatiere, difference);

        List<Parametre> parametresSatisfaits = new java.util.ArrayList<>();

        for (Parametre parametre : parametres) {
            if (parametre.getOperateur() == null || parametre.getResolution() == null) {
                logger.warn("Paramètre ID {} ignoré (Opérateur ou Résolution absent)", parametre.getId());
                continue;
            }

            String operateurCode = parametre.getOperateur().getOperateur();
            BigDecimal diffParam = parametre.getDiff();

            logger.debug("Évaluation paramètre: {} {} {} ?", difference, operateurCode, diffParam);

            boolean conditionSatisfaite = false;

            if ("<".equals(operateurCode)) {
                conditionSatisfaite = difference.compareTo(diffParam) < 0;
            } else if ("<=".equals(operateurCode)) {
                conditionSatisfaite = difference.compareTo(diffParam) <= 0;
            } else if (">".equals(operateurCode)) {
                conditionSatisfaite = difference.compareTo(diffParam) > 0;
            } else if (">=".equals(operateurCode)) {
                conditionSatisfaite = difference.compareTo(diffParam) >= 0;
            }

            if (conditionSatisfaite) {
                logger.info("Condition satisfaite pour paramètre ID={}.", parametre.getId());
                parametresSatisfaits.add(parametre);
            }
        }

        // Si plusieurs règles matchent (ou aucune), on cherche la limite (diff) la plus proche.
        // On cherche parmi les règles satisfaites. Si aucune n'est satisfaite (fallback),
        // on cherche parmi tous les paramètres.
        List<Parametre> listeAFiltrer = parametresSatisfaits.isEmpty() ? parametres : parametresSatisfaits;
        
        if (parametresSatisfaits.isEmpty()) {
            logger.info("Aucune condition matche. Recherche du paramètre le plus proche parmi tous (fallback).");
        } else if (parametresSatisfaits.size() > 1) {
            logger.info("Plusieurs conditions matchent. Recherche du paramètre le plus proche parmi les correspondants.");
        }

        Parametre parametreLePlusProche = null;
        BigDecimal distanceMin = null;
        boolean egalite = false;

        for (Parametre parametre : listeAFiltrer) {
            if (parametre.getResolution() == null) continue;

            BigDecimal distance = difference.subtract(parametre.getDiff()).abs();
            logger.debug("Paramètre ID {} → diff={}, distance={}", parametre.getId(), parametre.getDiff(), distance);

            if (distanceMin == null || distance.compareTo(distanceMin) < 0) {
                distanceMin = distance;
                parametreLePlusProche = parametre;
                egalite = false;
            } else if (distance.compareTo(distanceMin) == 0) {
                egalite = true;
            }
        }

        if (egalite) {
            // Distances égales → on prend la plus petite note
            logger.info("Distances égales entre plusieurs limites. Application de la plus petite note.");
            noteFinaleValue = noteMin;
        } else if (parametreLePlusProche != null) {
            logger.info("Paramètre le plus proche retenu : ID={}, diff={}, résolution={}.",
                    parametreLePlusProche.getId(),
                    parametreLePlusProche.getDiff(),
                    parametreLePlusProche.getResolution().getNom());
            noteFinaleValue = appliquerResolution(
                    parametreLePlusProche.getResolution().getNom(), notes, noteMin, noteMax);
        } else {
            // Garder la moyenne comme dernier recours si tous les paramètres sont invalides
            logger.warn("Aucun paramètre valide trouvé. Fallback sur la moyenne.");
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
        if (resolution == null) return calculerMoyenne(notes);
        
        String resTrim = resolution.trim();
        if (resTrim.equalsIgnoreCase("Petit")) return noteMin;
        if (resTrim.equalsIgnoreCase("Grand")) return noteMax;
        if (resTrim.equalsIgnoreCase("Moyenne")) return calculerMoyenne(notes);
        
        // Compatibilité avec les anciens noms si besoin
        if (resTrim.equalsIgnoreCase("plus_petit")) return noteMin;
        if (resTrim.equalsIgnoreCase("plus_grand")) return noteMax;
        if (resTrim.equalsIgnoreCase("moyenne")) return calculerMoyenne(notes);

        throw new RuntimeException("Résolution inconnue : [" + resolution + "]");
    }

    private BigDecimal calculerMoyenne(List<Note> notes) {
        BigDecimal somme = notes.stream()
                .map(Note::getNote)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return somme.divide(BigDecimal.valueOf(notes.size()), 2, RoundingMode.HALF_UP);
    }
}
