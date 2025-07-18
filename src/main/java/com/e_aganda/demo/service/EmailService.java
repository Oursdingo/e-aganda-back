package com.e_aganda.demo.service;

import com.e_aganda.demo.model.Collaborateur;
import com.e_aganda.demo.model.Projet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {
    // Service pour l'envoi d'emails de notification aux collaborateurs
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void envoyerNotificationCollaborateur(Collaborateur collaborateur, Projet projet) {
        // Logique d'envoi
        try{
            SimpleMailMessage message= new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(collaborateur.getEmail());
            message.setSubject("Nouveau projet : "+projet.getTitre());
            String contenu = String.format(
                    "Bonjour %s %s,\n\n" +
                            "Vous avez été ajouté comme collaborateur au projet \"%s\" par %s.\n\n" +
                            "Description du projet : %s\n" +
                            "Période : %s - %s\n\n" +
                            "Cordialement,\n" +
                            "L'équipe Switch Maker",
                    collaborateur.getPrenom(),
                    collaborateur.getNom(),
                    projet.getTitre(),
                    projet.getAuteur(),
                    projet.getDescription(),
                    projet.getDateDebut(),
                    projet.getDateFin()
            );
            message.setText(contenu);
            mailSender.send(message);
            logger.info("Email envoyé avec succès à : {}", collaborateur.getEmail());
        }catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email à {}: {}", collaborateur.getEmail(), e.getMessage());
        }


    }
    public void envoyerNotificationPourProjets (List<Collaborateur> collaborateurs, Projet projet) {
        // Envoi d'email à tous les collaborateurs du projet
        for (Collaborateur collaborateur : collaborateurs) {
            envoyerNotificationCollaborateur(collaborateur, projet);
        }
    }
}
