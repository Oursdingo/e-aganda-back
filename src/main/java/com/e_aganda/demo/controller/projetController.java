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
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@Tag(name = "Projet API", description = "Gestion de l'agenda collaboratif")
@RequestMapping("/api/projets")
public class projetController {

    @Autowired
    private ProjetService projetService;

    private static final Logger logger = LoggerFactory.getLogger(projetController.class);


    @GetMapping("/search")
    public ResponseEntity<ProjetDTO> searchBook(){
        return null;
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
        logger.info("Affichage des projets ");

        List<Projet> allProjets = projetService.getAllProjet();
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, allProjets.size());
        List<Projet> projetsPage = allProjets.subList(start, end);

        ProjetResponseDTO response = new ProjetResponseDTO();
        response.setProjets(projetsPage);
        response.setTotal(allProjets.size());

        return new ResponseEntity<>(response, HttpStatus.OK);
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
        Projet saved = projetService.saveProjetAvecCollaborateursEtTaches(projet);
        return new ResponseEntity<>(ProjetMapper.toDTO(saved), HttpStatus.CREATED);
    }
}
