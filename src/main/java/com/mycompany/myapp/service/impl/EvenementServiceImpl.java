package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Vote;
import com.mycompany.myapp.service.EvenementService;
import com.mycompany.myapp.domain.Evenement;
import com.mycompany.myapp.repository.EvenementRepository;
import com.mycompany.myapp.repository.search.EvenementSearchRepository;
import com.mycompany.myapp.service.dto.EvenementDTO;
import com.mycompany.myapp.service.mapper.EvenementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Evenement.
 */
@Service
@Transactional
public class EvenementServiceImpl implements EvenementService{

    private final Logger log = LoggerFactory.getLogger(EvenementServiceImpl.class);

    private final EvenementRepository evenementRepository;

    private final EvenementMapper evenementMapper;

    private final EvenementSearchRepository evenementSearchRepository;

    public EvenementServiceImpl(EvenementRepository evenementRepository, EvenementMapper evenementMapper, EvenementSearchRepository evenementSearchRepository) {
        this.evenementRepository = evenementRepository;
        this.evenementMapper = evenementMapper;
        this.evenementSearchRepository = evenementSearchRepository;
    }

    /**
     * Save a evenement.
     *
     * @param evenementDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EvenementDTO save(EvenementDTO evenementDTO) {
        log.debug("Request to save Evenement : {}", evenementDTO);
        Evenement evenement = evenementMapper.toEntity(evenementDTO);
        evenement = evenementRepository.save(evenement);
        EvenementDTO result = evenementMapper.toDto(evenement);
        evenementSearchRepository.save(evenement);
        return result;
    }

    /**
     *  Get all the evenements.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EvenementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Evenements");
        return evenementRepository.findAll(pageable)
            .map(evenementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvenementDTO> findAll(){
        return evenementRepository.findAll().stream().map(evenementMapper::toDto).collect(Collectors.toList());
    }

    /**
     *  Get one evenement by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public EvenementDTO findOne(Long id) {
        log.debug("Request to get Evenement : {}", id);
        Evenement evenement = evenementRepository.findOneWithEagerRelationships(id);
        return evenementMapper.toDto(evenement);
    }

    /**
     *  Delete the  evenement by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Evenement : {}", id);
        evenementRepository.delete(id);
        evenementSearchRepository.delete(id);
    }

    /**
     * Search for the evenement corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EvenementDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Evenements for query {}", query);
        Page<Evenement> result = evenementSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(evenementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvenementDTO> recupererParUtilisateurAyantAcces(String login){
        return evenementRepository.findByUtilisateurAyantAcces(login).stream().map(evenementMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvenementDTO> recupererEvenementsPourUtilisateurCourantPourVoter(){
        return evenementRepository.findEvenementsParUtilisateurNonVotant().stream().map(evenementMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EvenementDTO> recupererEvenementsPourUtilisateurCourantPourVoter(Pageable pageable){
        Page<Evenement> result = evenementRepository.findEvenementsParUtilisateurNonVotant(pageable);
        return result.map(evenementMapper::toDto);
    }
}
