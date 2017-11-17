package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.EvenementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Evenement.
 */
public interface EvenementService {

    /**
     * Save a evenement.
     *
     * @param evenementDTO the entity to save
     * @return the persisted entity
     */
    EvenementDTO save(EvenementDTO evenementDTO);

    /**
     *  Get all the evenements.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EvenementDTO> findAll(Pageable pageable);

    List<EvenementDTO> findAll();

    /**
     *  Get the "id" evenement.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EvenementDTO findOne(Long id);

    /**
     *  Delete the "id" evenement.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the evenement corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EvenementDTO> search(String query, Pageable pageable);

    List<EvenementDTO> recupererParUtilisateurAyantAcces(String login);
}
