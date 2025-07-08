package com.e_aganda.demo.dto;

import com.e_aganda.demo.model.Projet;

import java.util.List;


public class ProjetResponseDTO {
    private List<Projet> projets;
    private long total;

    public List<Projet> getProjets() {
        return projets;
    }

    public void setProjets(List<Projet> projets) {
        this.projets = projets;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public ProjetResponseDTO(List<Projet> projets, long total) {
        this.projets = projets;
        this.total = total;
    }

    public ProjetResponseDTO() {

    }
    // Constructeurs, getters, setters
}