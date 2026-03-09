package com.example.examen.service.impl;

import com.example.examen.model.entity.Matiere;
import com.example.examen.repository.MatiereRepository;
import com.example.examen.service.MatiereService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatiereServiceImpl implements MatiereService {

    private final MatiereRepository matiereRepository;

    public MatiereServiceImpl(MatiereRepository matiereRepository) {
        this.matiereRepository = matiereRepository;
    }

    @Override
    public List<Matiere> findAll() {
        return matiereRepository.findAll();
    }

    @Override
    public Optional<Matiere> findById(Long id) {
        return matiereRepository.findById(id);
    }

    @Override
    public Matiere save(Matiere matiere) {
        return matiereRepository.save(matiere);
    }

    @Override
    public Matiere update(Long id, Matiere matiere) {
        Matiere existing = matiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matière introuvable : id=" + id));
        existing.setNom(matiere.getNom());
        return matiereRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        matiereRepository.deleteById(id);
    }
}
