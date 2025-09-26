package com.e_aganda.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjetDTO {
    private Long id;
    private String auteur;
    private String titre;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDebut;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFin;
    private List<CollaborateurDTO> collaborateurs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;

    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public List<CollaborateurDTO> getCollaborateurs() {
        return collaborateurs;
    }

    public void setCollaborateurs(List<CollaborateurDTO> collaborateurs) {
        this.collaborateurs = collaborateurs;
    }
}
