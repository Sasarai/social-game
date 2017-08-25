package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.service.EvenementService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import com.mycompany.myapp.service.dto.EvenementDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Evenement.
 */
@RestController
@RequestMapping("/api")
public class EvenementResource {

    private final Logger log = LoggerFactory.getLogger(EvenementResource.class);

    private static final String ENTITY_NAME = "evenement";

    private final EvenementService evenementService;

    public EvenementResource(EvenementService evenementService) {
        this.evenementService = evenementService;
    }

    /**
     * POST  /evenements : Create a new evenement.
     *
     * @param evenementDTO the evenementDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new evenementDTO, or with status 400 (Bad Request) if the evenement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/evenements")
    @Timed
    public ResponseEntity<EvenementDTO> createEvenement(@Valid @RequestBody EvenementDTO evenementDTO) throws URISyntaxException {
        log.debug("REST request to save Evenement : {}", evenementDTO);
        if (evenementDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new evenement cannot already have an ID")).body(null);
        }
        EvenementDTO result = evenementService.save(evenementDTO);
        return ResponseEntity.created(new URI("/api/evenements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /evenements : Updates an existing evenement.
     *
     * @param evenementDTO the evenementDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated evenementDTO,
     * or with status 400 (Bad Request) if the evenementDTO is not valid,
     * or with status 500 (Internal Server Error) if the evenementDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/evenements")
    @Timed
    public ResponseEntity<EvenementDTO> updateEvenement(@Valid @RequestBody EvenementDTO evenementDTO) throws URISyntaxException {
        log.debug("REST request to update Evenement : {}", evenementDTO);
        if (evenementDTO.getId() == null) {
            return createEvenement(evenementDTO);
        }
        EvenementDTO result = evenementService.save(evenementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, evenementDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /evenements : get all the evenements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of evenements in body
     */
    @GetMapping("/evenements")
    @Timed
    public ResponseEntity<List<EvenementDTO>> getAllEvenements(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Evenements");
        Page<EvenementDTO> page = evenementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/evenements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /evenements/:id : get the "id" evenement.
     *
     * @param id the id of the evenementDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the evenementDTO, or with status 404 (Not Found)
     */
    @GetMapping("/evenements/{id}")
    @Timed
    public ResponseEntity<EvenementDTO> getEvenement(@PathVariable Long id) {
        log.debug("REST request to get Evenement : {}", id);
        EvenementDTO evenementDTO = evenementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(evenementDTO));
    }

    /**
     * DELETE  /evenements/:id : delete the "id" evenement.
     *
     * @param id the id of the evenementDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/evenements/{id}")
    @Timed
    public ResponseEntity<Void> deleteEvenement(@PathVariable Long id) {
        log.debug("REST request to delete Evenement : {}", id);
        evenementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/evenements?query=:query : search for the evenement corresponding
     * to the query.
     *
     * @param query the query of the evenement search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/evenements")
    @Timed
    public ResponseEntity<List<EvenementDTO>> searchEvenements(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Evenements for query {}", query);
        Page<EvenementDTO> page = evenementService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/evenements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
