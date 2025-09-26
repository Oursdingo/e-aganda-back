package com.e_aganda.demo.controller;

import com.e_aganda.demo.dto.ProjetDTO;
import com.e_aganda.demo.dto.ProjetMapper;
import com.e_aganda.demo.dto.ProjetResponseDTO;
import com.e_aganda.demo.model.Projet;
import com.e_aganda.demo.service.ProjetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@Tag(name = "Projet API", description = "Gestion de l'agenda collaboratif")
@RequestMapping("/api/projets")
public class projetController {

    @Autowired
    private ProjetService projetService;

    private static final Logger logger = LoggerFactory.getLogger(projetController.class);


    // Dans projetController.java
    @GetMapping("/search")
    public ResponseEntity<ProjetResponseDTO> searchProjets(
            @RequestParam("query") String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        try {
            logger.info("le nom rechercher dans la base est: {}",query);
            List<Projet> projets = projetService.searchProjets(query);

            // Calculer la pagination
            int totalProjets = projets.size();
            int start = (page - 1) * limit;
            int end = Math.min(start + limit, totalProjets);

            if (start >= totalProjets && totalProjets > 0) {
                // Page demandée au-delà des résultats
                ProjetResponseDTO emptyResponse = new ProjetResponseDTO();
                emptyResponse.setProjets(List.of());
                emptyResponse.setTotal(totalProjets);
                emptyResponse.setCurrentPage(page);
                emptyResponse.setTotalPages((int) Math.ceil((double) totalProjets / limit));
                return ResponseEntity.ok(emptyResponse);
            }

            List<Projet> subProjet = projets.subList(start, end);

            ProjetResponseDTO projetResponseDTO = new ProjetResponseDTO();
            projetResponseDTO.setProjets(subProjet);
            projetResponseDTO.setTotal(totalProjets);
            projetResponseDTO.setCurrentPage(page);
            projetResponseDTO.setTotalPages((int) Math.ceil((double) totalProjets / limit));

            return ResponseEntity.ok(projetResponseDTO);

        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de projets: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





    @Operation(summary = "Rechercher tous les projets", description = "Rechercher un projet avec ses collaborateurs et tâches.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Projet ajouté",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Projet.class))),
            @ApiResponse(responseCode = "400", description = "Erreur dans les données fournies",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<ProjetResponseDTO> getAllProjects(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        try {
            logger.info("Affichage des projets pour l'utilisateur connecté");

            List<Projet> allProjets = projetService.getProjetsPourUtilisateur();
            int totalProjets = allProjets.size();
            int start = (page - 1) * limit;
            int end = Math.min(start + limit, totalProjets);

            List<Projet> projetsPage = allProjets.subList(start, end);

            ProjetResponseDTO response = new ProjetResponseDTO();
            response.setProjets(projetsPage);
            response.setTotal(totalProjets);
            response.setCurrentPage(page);  // ⚠️ AJOUT
            response.setTotalPages((int) Math.ceil((double) totalProjets / limit)); // ⚠️ AJOUT

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des projets", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }








    @Operation(summary = "Ajouter un projet", description = "Ajoute un projet avec ses collaborateurs et tâches.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Projet ajouté",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Projet.class))),
            @ApiResponse(responseCode = "400", description = "Erreur dans les données fournies",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<ProjetDTO> createProjet(@RequestBody ProjetDTO projetDTO) {
        logger.info("Création d’un nouveau projet : {}", projetDTO.getTitre());
        Projet projet = ProjetMapper.toEntity(projetDTO);
        Projet saved = projetService.saveProjetPourUtilisateur(projet);
        return new ResponseEntity<>(ProjetMapper.toDTO(saved), HttpStatus.CREATED);
    }


    //By ai
    // Dans votre projetController.java - AJOUTEZ ces méthodes :

    @Operation(summary = "Modifier un projet")
    @PutMapping("/{id}")
    public ResponseEntity<ProjetDTO> updateProjet(
            @PathVariable Long id,
            @RequestBody @Valid ProjetDTO projetDTO) {

        logger.info("Modification du projet ID: {}", id);

        Projet projetExistant = projetService.getProjetById(id);

        // ⚠️ CORRECTION CRITIQUE : Supprimer cette ligne qui cause StackOverflowError
        // System.out.println("le projet existant a recuperer: "+projetExistant);

        if (projetExistant == null) {
            logger.warn("Projet avec ID {} non trouvé", id);
            return ResponseEntity.notFound().build();
        }

        // ⚠️ IMPORTANT : S'assurer que l'ID est bien défini dans le DTO
        projetDTO.setId(id);

        Projet projetModifie = ProjetMapper.toEntity(projetDTO);

        Projet saved = projetService.updateProjet(projetModifie);

        // ⚠️ CORRECTION CRITIQUE : Supprimer cette ligne aussi
        // System.out.println("le projet existant a modifier: "+saved);

        logger.info("Projet modifié avec succès - ID: {}, Titre: {}", saved.getId(), saved.getTitre());

        return ResponseEntity.ok(ProjetMapper.toDTO(saved));
    }

    @Operation(summary = "Supprimer un projet")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjet(@PathVariable Long id) {
        logger.info("Suppression du projet ID: {}", id);

        boolean deleted = projetService.deleteProjet(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Obtenir un projet par ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProjetDTO> getProjetById(@PathVariable Long id) {
        logger.info("Récupération du projet ID: {}", id);

        Projet projet = projetService.getProjetById(id);
        if (projet == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ProjetMapper.toDTO(projet));
    }



}
