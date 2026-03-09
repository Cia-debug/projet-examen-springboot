package com.example.examen.service.impl;

import com.example.examen.model.entity.Resolution;
import com.example.examen.repository.ResolutionRepository;
import com.example.examen.service.ResolutionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResolutionServiceImpl implements ResolutionService {

    private final ResolutionRepository resolutionRepository;

    public ResolutionServiceImpl(ResolutionRepository resolutionRepository) {
        this.resolutionRepository = resolutionRepository;
    }

    @Override
    public List<Resolution> findAll() {
        return resolutionRepository.findAll();
    }

    @Override
    public Optional<Resolution> findById(Long id) {
        return resolutionRepository.findById(id);
    }

    @Override
    public Resolution save(Resolution resolution) {
        return resolutionRepository.save(resolution);
    }

    @Override
    public Resolution update(Long id, Resolution resolution) {
        Resolution existing = resolutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Résolution introuvable : id=" + id));
        existing.setNom(resolution.getNom());
        return resolutionRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        resolutionRepository.deleteById(id);
    }
}
