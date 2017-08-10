package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Jeu;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Jeu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JeuRepository extends JpaRepository<Jeu,Long> {
    
}
