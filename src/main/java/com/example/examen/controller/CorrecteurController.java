package com.example.examen.controller;

import com.example.examen.model.entity.Correcteur;
import com.example.examen.service.CorrecteurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/correcteurs")
public class CorrecteurController {

    private final CorrecteurService correcteurService;

    public CorrecteurController(CorrecteurService correcteurService) {
        this.correcteurService = correcteurService;
    }

    @GetMapping
    public List<Correcteur> getAll() {
        return correcteurService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Correcteur> getById(@PathVariable Long id) {
        return correcteurService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Correcteur> create(@RequestBody Correcteur correcteur) {
        return ResponseEntity.ok(correcteurService.save(correcteur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Correcteur> update(@PathVariable Long id, @RequestBody Correcteur correcteur) {
        return ResponseEntity.ok(correcteurService.update(id, correcteur));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        correcteurService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
