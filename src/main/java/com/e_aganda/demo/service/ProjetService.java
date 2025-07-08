package com.e_aganda.demo.service;

import com.e_aganda.demo.model.Projet;
import com.e_aganda.demo.repository.ProjetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
public class ProjetService {
    @Autowired
    private ProjetRepository projetRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProjetService.class);

    @Transactional
    public Projet saveProjetAvecCollaborateursEtTaches(Projet projet) {
        projet.getCollaborateurs().forEach(collaborateur -> {
            collaborateur.setProjet(projet);
            collaborateur.getTaches().forEach(tache -> {
                tache.setCollaborateur(collaborateur);
            });
        });

        logger.info("Proje   t avec collaborateurs et tâches prêt à être sauvegardé.");
        return projetRepository.save(projet);
    }

    public List<Projet> getAllProjet(){
        return projetRepository.findAll().stream().sorted(Comparator.comparing( Projet::getTitre)).toList();
    }
}
