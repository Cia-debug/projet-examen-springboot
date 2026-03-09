package com.example.examen.controller;

import com.example.examen.model.entity.Parametre;
import com.example.examen.service.ParametreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parametres")
public class ParametreController {

    private final ParametreService parametreService;

    public ParametreController(ParametreService parametreService) {
        this.parametreService = parametreService;
    }

    @GetMapping
    public List<Parametre> getAll() {
        return parametreService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parametre> getById(@PathVariable Long id) {
        return parametreService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/matiere/{matiereId}")
    public List<Parametre> getByMatiere(@PathVariable Long matiereId) {
        return parametreService.findByMatiereId(matiereId);
    }

    @PostMapping
    public ResponseEntity<Parametre> create(@RequestBody Parametre parametre) {
        return ResponseEntity.ok(parametreService.save(parametre));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Parametre> update(@PathVariable Long id, @RequestBody Parametre parametre) {
        return ResponseEntity.ok(parametreService.update(id, parametre));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        parametreService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
