package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.service.JeuService;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import com.mycompany.myapp.service.dto.JeuDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Jeu.
 */
@RestController
@RequestMapping("/api")
public class JeuResource {

    private final Logger log = LoggerFactory.getLogger(JeuResource.class);

    private static final String ENTITY_NAME = "jeu";

    private final JeuService jeuService;

    private final UserService userService;

    public JeuResource(JeuService jeuService,
                       UserService userService) {
        this.jeuService = jeuService;
        this.userService = userService;
    }

    /**
     * POST  /jeus : Create a new jeu.
     *
     * @param jeuDTO the jeuDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jeuDTO, or with status 400 (Bad Request) if the jeu has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/jeus")
    @Timed
    public ResponseEntity<JeuDTO> createJeu(@RequestBody JeuDTO jeuDTO) throws URISyntaxException {
        log.debug("REST request to save Jeu : {}", jeuDTO);
        if (jeuDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jeu cannot already have an ID")).body(null);
        }

        if(jeuDTO.getProprietaireId() == null){
            if (userService.getUserWithAuthorities() != null) {
                jeuDTO.setProprietaireId(userService.getUserWithAuthorities().getId());
            }
        }

        JeuDTO result = jeuService.save(jeuDTO);
        return ResponseEntity.created(new URI("/api/jeus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /jeus : Updates an existing jeu.
     *
     * @param jeuDTO the jeuDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jeuDTO,
     * or with status 400 (Bad Request) if the jeuDTO is not valid,
     * or with status 500 (Internal Server Error) if the jeuDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/jeus")
    @Timed
    public ResponseEntity<JeuDTO> updateJeu(@RequestBody JeuDTO jeuDTO) throws URISyntaxException {
        log.debug("REST request to update Jeu : {}", jeuDTO);
        if (jeuDTO.getId() == null) {
            return createJeu(jeuDTO);
        }
        JeuDTO result = jeuService.save(jeuDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jeuDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /jeus : get all the jeus.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of jeus in body
     */
    @GetMapping("/jeus")
    @Timed
    public ResponseEntity<List<JeuDTO>> getAllJeus(@ApiParam Pageable pageable, @Param("type") String type) {
        log.debug("REST request to get a page of Jeus");

        Page<JeuDTO> page = null;
        if (type == null && (userService.getUserWithAuthorities() == null || userService.getUserWithAuthorities().isAdmin())) {
            page = jeuService.findAll(pageable);
        }
        else if("SPHERE".equals(type)) {
            page = jeuService.findJeuAuthorisesUtilisateur(pageable);
        }
        else {
            page = jeuService.findJeuUtilisateur(pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/jeus");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /jeus/:id : get the "id" jeu.
     *
     * @param id the id of the jeuDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jeuDTO, or with status 404 (Not Found)
     */
    @GetMapping("/jeus/{id}")
    @Timed
    public ResponseEntity<JeuDTO> getJeu(@PathVariable Long id) {
        log.debug("REST request to get Jeu : {}", id);
        JeuDTO jeuDTO = jeuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jeuDTO));
    }

    @GetMapping("_sphere/jeus/{idSphere}")
    @Timed
    public List<JeuDTO> getJeuxSphere(@PathVariable Long idSphere){
        log.debug("REST request to get sphere's games");
        return jeuService.recupererJeuxParIdSphere(idSphere);
    }

    /**
     * DELETE  /jeus/:id : delete the "id" jeu.
     *
     * @param id the id of the jeuDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/jeus/{id}")
    @Timed
    public ResponseEntity<Void> deleteJeu(@PathVariable Long id) {
        log.debug("REST request to delete Jeu : {}", id);
        jeuService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/jeus?query=:query : search for the jeu corresponding
     * to the query.
     *
     * @param query the query of the jeu search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/jeus")
    @Timed
    public ResponseEntity<List<JeuDTO>> searchJeus(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Jeus for query {}", query);
        Page<JeuDTO> page = jeuService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/jeus");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
