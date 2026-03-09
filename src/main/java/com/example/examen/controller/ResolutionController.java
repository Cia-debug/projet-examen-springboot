package com.example.examen.controller;

import com.example.examen.model.entity.Resolution;
import com.example.examen.service.ResolutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resolutions")
public class ResolutionController {

    private final ResolutionService resolutionService;

    public ResolutionController(ResolutionService resolutionService) {
        this.resolutionService = resolutionService;
    }

    @GetMapping
    public List<Resolution> getAll() {
        return resolutionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resolution> getById(@PathVariable Long id) {
        return resolutionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Resolution> create(@RequestBody Resolution resolution) {
        return ResponseEntity.ok(resolutionService.save(resolution));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resolution> update(@PathVariable Long id, @RequestBody Resolution resolution) {
        return ResponseEntity.ok(resolutionService.update(id, resolution));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        resolutionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
