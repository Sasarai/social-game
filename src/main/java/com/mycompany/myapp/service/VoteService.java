package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.VoteDTO;
import com.mycompany.myapp.web.rest.VoteResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Vote.
 */
public interface VoteService {

    /**
     * Save a vote.
     *
     * @param voteDTO the entity to save
     * @return the persisted entity
     */
    VoteDTO save(VoteDTO voteDTO);

    /**
     *  Get all the votes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<VoteDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" vote.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    VoteDTO findOne(Long id);

    /**
     *  Delete the "id" vote.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the vote corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<VoteDTO> search(String query, Pageable pageable);

    List<VoteDTO> getVotesPourEvenement(Long idEvenement);
}
