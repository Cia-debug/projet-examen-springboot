package com.example.examen.controller;

import com.example.examen.model.entity.Note;
import com.example.examen.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public List<Note> getAll() {
        return noteService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getById(@PathVariable Long id) {
        return noteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/candidat/{idCandidat}/matiere/{idMatiere}")
    public List<Note> getByCandidatAndMatiere(@PathVariable Long idCandidat,
                                              @PathVariable Long idMatiere) {
        return noteService.findByCandidatAndMatiere(idCandidat, idMatiere);
    }

    /**
     * Créer une note.
     * Body JSON :
     * { "idCandidat": 1, "idCorrecteur": 2, "idMatiere": 1, "note": 14.5 }
     */
    @PostMapping
    public ResponseEntity<Note> create(@RequestBody Map<String, Object> body) {
        Long idCandidat = Long.valueOf(body.get("idCandidat").toString());
        Long idCorrecteur = Long.valueOf(body.get("idCorrecteur").toString());
        Long idMatiere = Long.valueOf(body.get("idMatiere").toString());
        BigDecimal noteValue = new BigDecimal(body.get("note").toString());

        Note saved = noteService.save(idCandidat, idCorrecteur, idMatiere, noteValue);
        return ResponseEntity.ok(saved);
    }

    /**
     * Modifier une note.
     * Body JSON :
     * { "idCandidat": 1, "idCorrecteur": 2, "idMatiere": 1, "note": 15.0 }
     */
    @PutMapping("/{id}")
    public ResponseEntity<Note> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long idCandidat = Long.valueOf(body.get("idCandidat").toString());
        Long idCorrecteur = Long.valueOf(body.get("idCorrecteur").toString());
        Long idMatiere = Long.valueOf(body.get("idMatiere").toString());
        BigDecimal noteValue = new BigDecimal(body.get("note").toString());

        Note updated = noteService.update(id, idCandidat, idCorrecteur, idMatiere, noteValue);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
