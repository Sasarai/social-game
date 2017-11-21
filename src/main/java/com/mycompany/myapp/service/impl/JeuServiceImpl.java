package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.JeuService;
import com.mycompany.myapp.domain.Jeu;
import com.mycompany.myapp.repository.JeuRepository;
import com.mycompany.myapp.repository.search.JeuSearchRepository;
import com.mycompany.myapp.service.dto.JeuDTO;
import com.mycompany.myapp.service.mapper.JeuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Jeu.
 */
@Service
@Transactional
public class JeuServiceImpl implements JeuService{

    private final Logger log = LoggerFactory.getLogger(JeuServiceImpl.class);

    private final JeuRepository jeuRepository;

    private final JeuMapper jeuMapper;

    private final JeuSearchRepository jeuSearchRepository;

    public JeuServiceImpl(JeuRepository jeuRepository, JeuMapper jeuMapper, JeuSearchRepository jeuSearchRepository) {
        this.jeuRepository = jeuRepository;
        this.jeuMapper = jeuMapper;
        this.jeuSearchRepository = jeuSearchRepository;
    }

    /**
     * Save a jeu.
     *
     * @param jeuDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public JeuDTO save(JeuDTO jeuDTO) {
        log.debug("Request to save Jeu : {}", jeuDTO);
        Jeu jeu = jeuMapper.toEntity(jeuDTO);
        jeu = jeuRepository.save(jeu);
        JeuDTO result = jeuMapper.toDto(jeu);
        jeuSearchRepository.save(jeu);
        return result;
    }

    /**
     *  Get all the jeus.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JeuDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Jeus");
        return jeuRepository.findAll(pageable)
            .map(jeuMapper::toDto);
    }

    /**
     *  Get one jeu by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public JeuDTO findOne(Long id) {
        log.debug("Request to get Jeu : {}", id);
        Jeu jeu = jeuRepository.findOne(id);
        return jeuMapper.toDto(jeu);
    }

    /**
     *  Delete the  jeu by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Jeu : {}", id);
        jeuRepository.delete(id);
        jeuSearchRepository.delete(id);
    }

    /**
     * Search for the jeu corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JeuDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Jeus for query {}", query);
        Page<Jeu> result = jeuSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(jeuMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JeuDTO> findJeuUtilisateur(Pageable pageable){
        return jeuRepository.findByProprietaireIsCurrentUser(pageable).map(jeuMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JeuDTO> findJeuAuthorisesUtilisateur(Pageable pageable){
        return jeuRepository.findByUserAuthorise(pageable).map(jeuMapper::toDto);
    }
}
