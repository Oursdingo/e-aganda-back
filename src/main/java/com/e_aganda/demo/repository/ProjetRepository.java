package com.e_aganda.demo.repository;

import com.e_aganda.demo.model.Projet;
import com.e_aganda.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetRepository extends JpaRepository<Projet,Long> {
    java.util.List<Projet> findByUser(User user);
}
