package com.monapp.model;

import java.time.LocalDateTime;

public class Etudiant {

    private int id;
    private String matricule;
    private String nom;
    private String prenom;
    private String email;
    private int anneeId;
    private int groupeId;
    private LocalDateTime createdAt;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAnneeId() { return anneeId; }
    public void setAnneeId(int anneeId) { this.anneeId = anneeId; }

    public int getGroupeId() { return groupeId; }
    public void setGroupeId(int groupeId) { this.groupeId = groupeId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
