package com.monapp.model;

import java.time.LocalDateTime;

public class Annee {

    private int id;
    private String nom;
    private LocalDateTime createdAt;

    public Annee() {}

    public Annee(int id, String nom, LocalDateTime createdAt) {
        this.id = id;
        this.nom = nom;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return nom;
    }
}
