package com.example.examen.controller;

import com.example.examen.model.entity.NoteFinale;
import com.example.examen.service.NoteFinaleServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class NoteFinaleController {

    private final NoteFinaleServiceInterface noteFinaleService;

    public NoteFinaleController(NoteFinaleServiceInterface noteFinaleService) {
        this.noteFinaleService = noteFinaleService;
    }

    @GetMapping("/notes-finales")
    public List<NoteFinale> getAll() {
        return noteFinaleService.findAll();
    }

    /**
     * POST /calcul-note-finale/{candidat}/{matiere}
     * Déclencher le calcul de la note finale.
     */
    @PostMapping("/calcul-note-finale/{candidat}/{matiere}")
    public ResponseEntity<Map<String, Object>> calculer(
            @PathVariable("candidat") Long idCandidat,
            @PathVariable("matiere") Long idMatiere) {

        NoteFinale noteFinale = noteFinaleService.calculerNoteFinale(idCandidat, idMatiere);
        return ResponseEntity.ok(toMap(noteFinale));
    }

    /**
     * GET /note-finale/{candidat}/{matiere}
     * Récupérer la note finale d'un candidat pour une matière.
     */
    @GetMapping("/note-finale/{candidat}/{matiere}")
    public ResponseEntity<Map<String, Object>> getNoteFinale(
            @PathVariable("candidat") Long idCandidat,
            @PathVariable("matiere") Long idMatiere) {

        NoteFinale noteFinale = noteFinaleService
                .findByCandidatAndMatiere(idCandidat, idMatiere)
                .orElseThrow(() -> new RuntimeException(
                        "Note finale introuvable pour candidat=" + idCandidat
                                + ", matière=" + idMatiere));

        return ResponseEntity.ok(toMap(noteFinale));
    }

    @DeleteMapping("/notes-finales/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noteFinaleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Map<String, Object> toMap(NoteFinale nf) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", nf.getId());
        response.put("idCandidat", nf.getCandidat().getId());
        response.put("nomCandidat", nf.getCandidat().getNom());
        response.put("idMatiere", nf.getMatiere().getId());
        response.put("nomMatiere", nf.getMatiere().getNom());
        response.put("noteFinale", nf.getNoteFinale());
        return response;
    }
}
