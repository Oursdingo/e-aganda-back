package com.e_aganda.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class CollaborateurDTO {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private List<TacheDTO> taches;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<TacheDTO> getTaches() {
        return taches;
    }

    public void setTaches(List<TacheDTO> taches) {
        this.taches = taches;
    }
}
