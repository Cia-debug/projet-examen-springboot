package com.example.examen.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "t_candidat")
public class Candidat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, length = 20)
    private String numero;

    @JsonIgnore
    @OneToMany(mappedBy = "candidat")
    private List<Note> notes;

    @JsonIgnore
    @OneToMany(mappedBy = "candidat")
    private List<NoteFinale> notesFinales;

    public Candidat() {}

    public Candidat(String nom, String numero) {
        this.nom = nom;
        this.numero = numero;
    }

    // Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public List<Note> getNotes() { return notes; }
    public void setNotes(List<Note> notes) { this.notes = notes; }

    public List<NoteFinale> getNotesFinales() { return notesFinales; }
    public void setNotesFinales(List<NoteFinale> notesFinales) { this.notesFinales = notesFinales; }
}
