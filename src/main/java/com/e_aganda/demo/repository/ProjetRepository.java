package com.e_aganda.demo.repository;

import com.e_aganda.demo.model.Projet;
import com.e_aganda.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjetRepository extends JpaRepository<Projet,Long> {
    java.util.List<Projet> findByUser(User user);
    @Query("SELECT p FROM Projet p WHERE p.user.id = :userId AND " +
            "(LOWER(p.titre) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.auteur) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Projet> searchByTitreOrAuteurAndUser(@Param("query") String query, @Param("userId") Long userId);
    // Dans votre ProjetRepository.java - AJOUTEZ cette méthode :

    Optional<Projet> findByIdAndUser(Long id, User user);

}
