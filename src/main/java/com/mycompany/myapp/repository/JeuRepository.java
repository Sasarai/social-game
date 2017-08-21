package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Jeu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Jeu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JeuRepository extends JpaRepository<Jeu,Long> {

    @Query("select jeu from Jeu jeu where jeu.proprietaire.login = ?#{principal.username}")
    List<Jeu> findByProprietaireIsCurrentUser();

    @Query("select jeu from Jeu jeu where jeu.proprietaire.login = ?#{principal.username}")
    Page<Jeu> findByProprietaireIsCurrentUser(Pageable pageable);

}