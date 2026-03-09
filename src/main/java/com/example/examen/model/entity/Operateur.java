package com.example.examen.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "t_operateur")
public class Operateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String operateur;

    public Operateur() {}

    public Operateur(String operateur) {
        this.operateur = operateur;
    }

    // Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOperateur() { return operateur; }
    public void setOperateur(String operateur) { this.operateur = operateur; }
}
