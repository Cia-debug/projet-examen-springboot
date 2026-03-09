package com.example.examen.service.impl;

import com.example.examen.model.entity.Operateur;
import com.example.examen.repository.OperateurRepository;
import com.example.examen.service.OperateurService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OperateurServiceImpl implements OperateurService {

    private final OperateurRepository operateurRepository;

    public OperateurServiceImpl(OperateurRepository operateurRepository) {
        this.operateurRepository = operateurRepository;
    }

    @Override
    public List<Operateur> findAll() {
        return operateurRepository.findAll();
    }

    @Override
    public Optional<Operateur> findById(Long id) {
        return operateurRepository.findById(id);
    }

    @Override
    public Operateur save(Operateur operateur) {
        return operateurRepository.save(operateur);
    }

    @Override
    public Operateur update(Long id, Operateur operateur) {
        Operateur existing = operateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opérateur introuvable : id=" + id));
        existing.setOperateur(operateur.getOperateur());
        return operateurRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        operateurRepository.deleteById(id);
    }
}
