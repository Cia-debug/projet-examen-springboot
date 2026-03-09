package com.example.examen.controller;

import com.example.examen.model.entity.Matiere;
import com.example.examen.service.MatiereService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matieres")
public class MatiereController {

    private final MatiereService matiereService;

    public MatiereController(MatiereService matiereService) {
        this.matiereService = matiereService;
    }

    @GetMapping
    public List<Matiere> getAll() {
        return matiereService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Matiere> getById(@PathVariable Long id) {
        return matiereService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Matiere> create(@RequestBody Matiere matiere) {
        return ResponseEntity.ok(matiereService.save(matiere));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Matiere> update(@PathVariable Long id, @RequestBody Matiere matiere) {
        return ResponseEntity.ok(matiereService.update(id, matiere));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        matiereService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
