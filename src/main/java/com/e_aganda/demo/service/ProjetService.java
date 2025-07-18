package com.e_aganda.demo.service;

import com.e_aganda.demo.dto.ProjetDTO;
import com.e_aganda.demo.model.Collaborateur;
import com.e_aganda.demo.model.Projet;
import com.e_aganda.demo.model.User;
import com.e_aganda.demo.repository.ProjetRepository;
import com.e_aganda.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProjetService {
    @Autowired
    private ProjetRepository projetRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(ProjetService.class);

    @Transactional
    public Projet saveProjetAvecCollaborateursEtTaches(Projet projet) {
        projet.getCollaborateurs().forEach(collaborateur -> {
            collaborateur.setProjet(projet);
            collaborateur.getTaches().forEach(tache -> {
                tache.setCollaborateur(collaborateur);
            });
        });

        logger.info("Projet avec collaborateurs et tâches prêt à être sauvegardé.");
        return projetRepository.save(projet);
    }

    @Transactional
    public Projet saveProjetPourUtilisateur(Projet projet) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        projet.setUser(user);
        projet.getCollaborateurs().forEach(collaborateur -> {
            collaborateur.setProjet(projet);
            collaborateur.getTaches().forEach(tache -> {
                tache.setCollaborateur(collaborateur);
            });
        });
        Projet savedProjet =  projetRepository.save(projet);
        try{
            // Envoi d'email de notification aux collaborateurs
            emailService.envoyerNotificationPourProjets(savedProjet.getCollaborateurs(), savedProjet);
            logger.info("Email de notification envoyé pour le projet : {}", savedProjet.getTitre());
        } catch (Exception e) {
            logger.warn("Erreur lors de l'envoi des emails pour le projet {}: {}",
                    savedProjet.getTitre(), e.getMessage());
            // On ne fait pas échouer la transaction si l'email ratelogger.warn("Erreur lors de l'envoi des emails pour le projet {}: {}",
            //                       savedProjet.getTitre(), e.getMessage());
            //            // On ne fait pas échouer la transaction si l'email rate
        }
        return savedProjet;
    }

    public List<Projet> getProjetsPourUtilisateur() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return projetRepository.findByUser(user).stream().sorted(Comparator.comparing(Projet::getTitre)).toList();
    }
    // Dans ProjetService.java
    public List<Projet> searchProjets(String query) { // Sans userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        logger.info("La valeur de l'id du compte rechercher est :{}",user.getId());
        return projetRepository.searchByTitreOrAuteurAndUser(query, user.getId());
    }


    //by ai
    // Dans votre ProjetService.java - AJOUTEZ ces méthodes :

    public Projet getProjetById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return projetRepository.findByIdAndUser(id, user)
                .orElse(null);
    }

    @Transactional
    public Projet updateProjet(Projet projet) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        projet.setUser(user);

        // Récupérer l'ancien projet pour comparer les collaborateurs
        Projet ancienProjet = projetRepository.findByIdAndUser(projet.getId(), user)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        // Gérer les collaborateurs et tâches
        projet.getCollaborateurs().forEach(collaborateur -> {
            collaborateur.setProjet(projet);
            collaborateur.getTaches().forEach(tache -> {
                tache.setCollaborateur(collaborateur);
            });
        });

        Projet saveProjet = projetRepository.save(projet);
        try {
            List<String> anciensEmails = ancienProjet.getCollaborateurs().stream()
                    .map(Collaborateur::getEmail)
                    .toList();
            List<Collaborateur> nouveauxCollaborateurs = saveProjet.getCollaborateurs().stream()
                    .filter(c -> !anciensEmails.contains(c.getEmail()))
                    .toList();
            if (!nouveauxCollaborateurs.isEmpty()) {
                emailService.envoyerNotificationPourProjets(nouveauxCollaborateurs,saveProjet);
                logger.info("Email de notification envoyé pour les nouveaux collaborateurs du projet : {}", saveProjet.getTitre());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return saveProjet;
    }

    @Transactional
    public boolean deleteProjet(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Optional<Projet> projet = projetRepository.findByIdAndUser(id, user);
        if (projet.isPresent()) {
            projetRepository.delete(projet.get());
            return true;
        }
        return false;
    }

}
