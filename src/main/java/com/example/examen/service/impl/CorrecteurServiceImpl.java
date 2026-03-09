package com.example.examen.service.impl;

import com.example.examen.model.entity.Correcteur;
import com.example.examen.repository.CorrecteurRepository;
import com.example.examen.service.CorrecteurService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CorrecteurServiceImpl implements CorrecteurService {

    private final CorrecteurRepository correcteurRepository;

    public CorrecteurServiceImpl(CorrecteurRepository correcteurRepository) {
        this.correcteurRepository = correcteurRepository;
    }

    @Override
    public List<Correcteur> findAll() {
        return correcteurRepository.findAll();
    }

    @Override
    public Optional<Correcteur> findById(Long id) {
        return correcteurRepository.findById(id);
    }

    @Override
    public Correcteur save(Correcteur correcteur) {
        return correcteurRepository.save(correcteur);
    }

    @Override
    public Correcteur update(Long id, Correcteur correcteur) {
        Correcteur existing = correcteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Correcteur introuvable : id=" + id));
        existing.setNom(correcteur.getNom());
        return correcteurRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        correcteurRepository.deleteById(id);
    }
}
