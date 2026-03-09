package com.example.examen.repository;

import com.example.examen.model.entity.NoteFinale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoteFinaleRepository extends JpaRepository<NoteFinale, Long> {

    Optional<NoteFinale> findByCandidatIdAndMatiereId(Long candidatId, Long matiereId);
}
