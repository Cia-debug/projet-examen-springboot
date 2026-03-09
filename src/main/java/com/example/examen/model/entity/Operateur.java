package com.example.examen.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "t_operateur")
public class Operateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer operateur;

    public Operateur() {}

    public Operateur(Integer operateur) {
        this.operateur = operateur;
    }

    // Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getOperateur() { return operateur; }
    public void setOperateur(Integer operateur) { this.operateur = operateur; }
}
