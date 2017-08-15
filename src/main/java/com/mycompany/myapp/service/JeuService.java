package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.JeuDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Jeu.
 */
public interface JeuService {

    /**
     * Save a jeu.
     *
     * @param jeuDTO the entity to save
     * @return the persisted entity
     */
    JeuDTO save(JeuDTO jeuDTO);

    /**
     *  Get all the jeus.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<JeuDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" jeu.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    JeuDTO findOne(Long id);

    /**
     *  Delete the "id" jeu.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the jeu corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<JeuDTO> search(String query, Pageable pageable);

    Page<JeuDTO> findJeuUtilisateur(Pageable pageable);
}
