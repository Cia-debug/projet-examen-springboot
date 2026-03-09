package com.example.examen.service.impl;

import com.example.examen.model.entity.Candidat;
import com.example.examen.repository.CandidatRepository;
import com.example.examen.service.CandidatService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidatServiceImpl implements CandidatService {

    private final CandidatRepository candidatRepository;

    public CandidatServiceImpl(CandidatRepository candidatRepository) {
        this.candidatRepository = candidatRepository;
    }

    @Override
    public List<Candidat> findAll() {
        return candidatRepository.findAll();
    }

    @Override
    public Optional<Candidat> findById(Long id) {
        return candidatRepository.findById(id);
    }

    @Override
    public Candidat save(Candidat candidat) {
        return candidatRepository.save(candidat);
    }

    @Override
    public Candidat update(Long id, Candidat candidat) {
        Candidat existing = candidatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidat introuvable : id=" + id));
        existing.setNom(candidat.getNom());
        existing.setNumero(candidat.getNumero());
        return candidatRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        candidatRepository.deleteById(id);
    }
}
