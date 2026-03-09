package com.example.examen.controller;

import com.example.examen.model.entity.Operateur;
import com.example.examen.service.OperateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operateurs")
public class OperateurController {

    private final OperateurService operateurService;

    public OperateurController(OperateurService operateurService) {
        this.operateurService = operateurService;
    }

    @GetMapping
    public List<Operateur> getAll() {
        return operateurService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Operateur> getById(@PathVariable Long id) {
        return operateurService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Operateur> create(@RequestBody Operateur operateur) {
        return ResponseEntity.ok(operateurService.save(operateur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Operateur> update(@PathVariable Long id, @RequestBody Operateur operateur) {
        return ResponseEntity.ok(operateurService.update(id, operateur));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        operateurService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
