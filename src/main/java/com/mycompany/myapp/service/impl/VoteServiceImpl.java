package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.VoteService;
import com.mycompany.myapp.domain.Vote;
import com.mycompany.myapp.repository.VoteRepository;
import com.mycompany.myapp.repository.search.VoteSearchRepository;
import com.mycompany.myapp.service.dto.VoteDTO;
import com.mycompany.myapp.service.mapper.VoteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Vote.
 */
@Service
@Transactional
public class VoteServiceImpl implements VoteService{

    private final Logger log = LoggerFactory.getLogger(VoteServiceImpl.class);

    private final VoteRepository voteRepository;

    private final VoteMapper voteMapper;

    private final VoteSearchRepository voteSearchRepository;

    public VoteServiceImpl(VoteRepository voteRepository, VoteMapper voteMapper, VoteSearchRepository voteSearchRepository) {
        this.voteRepository = voteRepository;
        this.voteMapper = voteMapper;
        this.voteSearchRepository = voteSearchRepository;
    }

    /**
     * Save a vote.
     *
     * @param voteDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public VoteDTO save(VoteDTO voteDTO) {
        log.debug("Request to save Vote : {}", voteDTO);
        Vote vote = voteMapper.toEntity(voteDTO);
        vote = voteRepository.save(vote);
        VoteDTO result = voteMapper.toDto(vote);
        voteSearchRepository.save(vote);
        return result;
    }

    /**
     *  Get all the votes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VoteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Votes");
        return voteRepository.findAll(pageable)
            .map(voteMapper::toDto);
    }

    /**
     *  Get one vote by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public VoteDTO findOne(Long id) {
        log.debug("Request to get Vote : {}", id);
        Vote vote = voteRepository.findOne(id);
        return voteMapper.toDto(vote);
    }

    /**
     *  Delete the  vote by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vote : {}", id);
        voteRepository.delete(id);
        voteSearchRepository.delete(id);
    }

    /**
     * Search for the vote corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VoteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Votes for query {}", query);
        Page<Vote> result = voteSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(voteMapper::toDto);
    }
}
