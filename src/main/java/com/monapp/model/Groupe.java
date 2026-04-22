package com.monapp.model;

import java.time.LocalDateTime;

public class Groupe {

    private int id;
    private int anneeId;
    private String nom;
    private LocalDateTime createdAt;

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAnneeId() { return anneeId; }
    public void setAnneeId(int anneeId) { this.anneeId = anneeId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
