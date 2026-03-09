package com.example.examen.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "t_matiere")
public class Matiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @JsonIgnore
    @OneToMany(mappedBy = "matiere")
    private List<Note> notes;

    @JsonIgnore
    @OneToMany(mappedBy = "matiere")
    private List<Parametre> parametres;

    @JsonIgnore
    @OneToMany(mappedBy = "matiere")
    private List<NoteFinale> notesFinales;

    public Matiere() {}

    public Matiere(String nom) {
        this.nom = nom;
    }

    // Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public List<Note> getNotes() { return notes; }
    public void setNotes(List<Note> notes) { this.notes = notes; }

    public List<Parametre> getParametres() { return parametres; }
    public void setParametres(List<Parametre> parametres) { this.parametres = parametres; }

    public List<NoteFinale> getNotesFinales() { return notesFinales; }
    public void setNotesFinales(List<NoteFinale> notesFinales) { this.notesFinales = notesFinales; }
}
