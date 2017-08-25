package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.SphereService;
import com.mycompany.myapp.domain.Sphere;
import com.mycompany.myapp.repository.SphereRepository;
import com.mycompany.myapp.repository.search.SphereSearchRepository;
import com.mycompany.myapp.service.dto.SphereDTO;
import com.mycompany.myapp.service.mapper.SphereMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Sphere.
 */
@Service
@Transactional
public class SphereServiceImpl implements SphereService{

    private final Logger log = LoggerFactory.getLogger(SphereServiceImpl.class);

    private final SphereRepository sphereRepository;

    private final SphereMapper sphereMapper;

    private final SphereSearchRepository sphereSearchRepository;

    public SphereServiceImpl(SphereRepository sphereRepository, SphereMapper sphereMapper, SphereSearchRepository sphereSearchRepository) {
        this.sphereRepository = sphereRepository;
        this.sphereMapper = sphereMapper;
        this.sphereSearchRepository = sphereSearchRepository;
    }

    /**
     * Save a sphere.
     *
     * @param sphereDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SphereDTO save(SphereDTO sphereDTO) {
        log.debug("Request to save Sphere : {}", sphereDTO);
        Sphere sphere = sphereMapper.toEntity(sphereDTO);
        sphere = sphereRepository.save(sphere);
        SphereDTO result = sphereMapper.toDto(sphere);
        sphereSearchRepository.save(sphere);
        return result;
    }

    /**
     *  Get all the spheres.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SphereDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Spheres");
        return sphereRepository.findAll(pageable)
            .map(sphereMapper::toDto);
    }

    /**
     *  Get one sphere by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SphereDTO findOne(Long id) {
        log.debug("Request to get Sphere : {}", id);
        Sphere sphere = sphereRepository.findOneWithEagerRelationships(id);
        return sphereMapper.toDto(sphere);
    }

    /**
     *  Delete the  sphere by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sphere : {}", id);
        sphereRepository.delete(id);
        sphereSearchRepository.delete(id);
    }

    /**
     * Search for the sphere corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SphereDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Spheres for query {}", query);
        Page<Sphere> result = sphereSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(sphereMapper::toDto);
    }
}
