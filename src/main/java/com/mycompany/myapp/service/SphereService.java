package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.SphereDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Sphere.
 */
public interface SphereService {

    /**
     * Save a sphere.
     *
     * @param sphereDTO the entity to save
     * @return the persisted entity
     */
    SphereDTO save(SphereDTO sphereDTO);

    /**
     *  Get all the spheres.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SphereDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" sphere.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SphereDTO findOne(Long id);

    /**
     *  Delete the "id" sphere.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the sphere corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SphereDTO> search(String query, Pageable pageable);

    SphereDTO abonnement(SphereDTO sphereDTO, String loginUtilisateur);

    SphereDTO desabonnement(SphereDTO sphereDTO, String loginUtilisateur);
}
