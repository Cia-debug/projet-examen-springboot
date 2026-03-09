package com.example.examen.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "t_note")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_candidat", nullable = false)
    private Candidat candidat;

    @ManyToOne
    @JoinColumn(name = "id_correcteur", nullable = false)
    private Correcteur correcteur;

    @ManyToOne
    @JoinColumn(name = "id_matiere", nullable = false)
    private Matiere matiere;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal note;

    public Note() {}

    public Note(Candidat candidat, Correcteur correcteur, Matiere matiere, BigDecimal note) {
        this.candidat = candidat;
        this.correcteur = correcteur;
        this.matiere = matiere;
        this.note = note;
    }

    // Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Candidat getCandidat() { return candidat; }
    public void setCandidat(Candidat candidat) { this.candidat = candidat; }

    public Correcteur getCorrecteur() { return correcteur; }
    public void setCorrecteur(Correcteur correcteur) { this.correcteur = correcteur; }

    public Matiere getMatiere() { return matiere; }
    public void setMatiere(Matiere matiere) { this.matiere = matiere; }

    public BigDecimal getNote() { return note; }
    public void setNote(BigDecimal note) { this.note = note; }
}
