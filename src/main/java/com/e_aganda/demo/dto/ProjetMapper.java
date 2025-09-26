package com.e_aganda.demo.dto;

import com.e_aganda.demo.model.Collaborateur;
import com.e_aganda.demo.model.Projet;
import com.e_aganda.demo.model.Tache;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjetMapper {

    public static ProjetDTO toDTO(Projet projet) {
        if (projet == null) {
            return null;
        }

        ProjetDTO dto = new ProjetDTO();
        dto.setId(projet.getId());
        dto.setAuteur(projet.getAuteur());
        dto.setTitre(projet.getTitre());
        dto.setDescription(projet.getDescription());
        dto.setDateDebut(projet.getDateDebut());
        dto.setDateFin(projet.getDateFin());

        // ⚠️ CORRECTION : Vérification null pour éviter NullPointerException
        if (projet.getCollaborateurs() != null) {
            dto.setCollaborateurs(
                    projet.getCollaborateurs().stream().map(c -> {
                        CollaborateurDTO cDTO = new CollaborateurDTO();
                        cDTO.setId(c.getId());
                        cDTO.setNom(c.getNom());
                        cDTO.setPrenom(c.getPrenom());
                        cDTO.setEmail(c.getEmail());

                        // ⚠️ CORRECTION : Vérification null pour les tâches
                        if (c.getTaches() != null) {
                            cDTO.setTaches(
                                    c.getTaches().stream().map(t -> {
                                        TacheDTO tacheDTO = new TacheDTO();
                                        tacheDTO.setId(t.getId());
                                        tacheDTO.setTitre(t.getTitre());
                                        tacheDTO.setDescription(t.getDescription());
                                        tacheDTO.setDateDebut(t.getDateDebut());
                                        tacheDTO.setDateFin(t.getDateFin());
                                        tacheDTO.setStatut(t.getStatut());
                                        return tacheDTO;
                                    }).toList()
                            );
                        } else {
                            cDTO.setTaches(new ArrayList<>());
                        }

                        return cDTO;
                    }).toList()
            );
        } else {
            dto.setCollaborateurs(new ArrayList<>());
        }

        return dto;
    }

    public static Projet toEntity(ProjetDTO dto) {
        if (dto == null) {
            return null;
        }

        Projet projet = new Projet();
        projet.setId(dto.getId());
        projet.setAuteur(dto.getAuteur());
        projet.setTitre(dto.getTitre());
        projet.setDescription(dto.getDescription());
        projet.setDateDebut(dto.getDateDebut());
        projet.setDateFin(dto.getDateFin());

        if (dto.getCollaborateurs() != null) {
            List<Collaborateur> collaborateurs = dto.getCollaborateurs().stream().map(cDTO -> {
                Collaborateur c = new Collaborateur();
                c.setId(cDTO.getId());
                c.setNom(cDTO.getNom());
                c.setPrenom(cDTO.getPrenom());
                c.setEmail(cDTO.getEmail());
                // ⚠️ IMPORTANT : Établir la relation bidirectionnelle
                c.setProjet(projet);

                // ⚠️ CORRECTION : Gestion améliorée des tâches
                List<Tache> taches = new ArrayList<>();
                if (cDTO.getTaches() != null) {
                    taches = cDTO.getTaches().stream().map(tDTO -> {
                        Tache t = new Tache();
                        t.setId(tDTO.getId());
                        t.setTitre(tDTO.getTitre());
                        t.setDescription(tDTO.getDescription());
                        t.setDateDebut(tDTO.getDateDebut());
                        t.setDateFin(tDTO.getDateFin());
                        t.setStatut(tDTO.getStatut());
                        // ⚠️ IMPORTANT : Établir la relation bidirectionnelle
                        t.setCollaborateur(c);
                        return t;
                    }).collect(Collectors.toList());
                }

                c.setTaches(taches);
                return c;
            }).collect(Collectors.toList());

            projet.setCollaborateurs(collaborateurs);
        } else {
            projet.setCollaborateurs(new ArrayList<>());
        }

        return projet;
    }
}