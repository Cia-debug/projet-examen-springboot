package com.example.examen.service.impl;

import com.example.examen.model.entity.Parametre;
import com.example.examen.repository.ParametreRepository;
import com.example.examen.service.ParametreService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParametreServiceImpl implements ParametreService {

    private final ParametreRepository parametreRepository;

    public ParametreServiceImpl(ParametreRepository parametreRepository) {
        this.parametreRepository = parametreRepository;
    }

    @Override
    public List<Parametre> findAll() {
        return parametreRepository.findAll();
    }

    @Override
    public Optional<Parametre> findById(Long id) {
        return parametreRepository.findById(id);
    }

    @Override
    public List<Parametre> findByMatiereId(Long matiereId) {
        return parametreRepository.findByMatiereIdOrderByIdAsc(matiereId);
    }

    @Override
    public Parametre save(Parametre parametre) {
        return parametreRepository.save(parametre);
    }

    @Override
    public Parametre update(Long id, Parametre parametre) {
        Parametre existing = parametreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paramètre introuvable : id=" + id));
        existing.setMatiere(parametre.getMatiere());
        existing.setDiff(parametre.getDiff());
        existing.setOperateur(parametre.getOperateur());
        existing.setResolution(parametre.getResolution());
        return parametreRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        parametreRepository.deleteById(id);
    }
}
