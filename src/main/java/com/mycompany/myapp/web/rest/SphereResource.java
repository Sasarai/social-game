package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Sphere;
import com.mycompany.myapp.service.SphereService;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import com.mycompany.myapp.service.dto.SphereDTO;
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
 * REST controller for managing Sphere.
 */
@RestController
@RequestMapping("/api")
public class SphereResource {

    private final Logger log = LoggerFactory.getLogger(SphereResource.class);

    private static final String ENTITY_NAME = "sphere";

    private final SphereService sphereService;

    private final UserService userService;

    public SphereResource(
        SphereService sphereService,
        UserService userService
    ) {
        this.sphereService = sphereService;
        this.userService = userService;
    }

    /**
     * POST  /spheres : Create a new sphere.
     *
     * @param sphereDTO the sphereDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sphereDTO, or with status 400 (Bad Request) if the sphere has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/spheres")
    @Timed
    public ResponseEntity<SphereDTO> createSphere(@RequestBody SphereDTO sphereDTO) throws URISyntaxException {
        log.debug("REST request to save Sphere : {}", sphereDTO);
        if (sphereDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sphere cannot already have an ID")).body(null);
        }

        if(userService.getUserWithAuthorities() != null){
            sphereDTO.setAdministrateurId(userService.getUserWithAuthorities().getId());
        }

        SphereDTO result = sphereService.save(sphereDTO);
        return ResponseEntity.created(new URI("/api/spheres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getNom()))
            .body(result);
    }

    /**
     * PUT  /spheres : Updates an existing sphere.
     *
     * @param sphereDTO the sphereDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sphereDTO,
     * or with status 400 (Bad Request) if the sphereDTO is not valid,
     * or with status 500 (Internal Server Error) if the sphereDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/spheres")
    @Timed
    public ResponseEntity<SphereDTO> updateSphere(@RequestBody SphereDTO sphereDTO) throws URISyntaxException {
        log.debug("REST request to update Sphere : {}", sphereDTO);
        if (sphereDTO.getId() == null) {
            return createSphere(sphereDTO);
        }
        SphereDTO result = sphereService.save(sphereDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sphereDTO.getNom()))
            .body(result);
    }

    @PutMapping("/join/sphere/{loginUtilisateur}")
    @Timed
    public ResponseEntity<SphereDTO> joinSphere(@RequestBody SphereDTO sphereDTO, @PathVariable("loginUtilisateur") String loginUtilisateur) throws URISyntaxException {
        log.debug("REST request to join sphere : {}", sphereDTO);

        SphereDTO result = sphereService.abonnement(sphereDTO, loginUtilisateur);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityJoinAlert(ENTITY_NAME, sphereDTO.getNom()))
            .body(result);

    }

    @PutMapping("/quit/sphere/{loginUtilisateur}")
    @Timed
    public ResponseEntity<SphereDTO> quitSphere(@RequestBody SphereDTO sphereDTO, @PathVariable("loginUtilisateur") String loginUtilisateur) throws URISyntaxException {
        log.debug("REST request to join sphere : {}", sphereDTO);

        SphereDTO result = sphereService.desabonnement(sphereDTO, loginUtilisateur);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityQuitAlert(ENTITY_NAME, sphereDTO.getNom()))
            .body(result);

    }

    /**
     * GET  /spheres : get all the spheres.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of spheres in body
     */
    @GetMapping("/spheres")
    @Timed
    public ResponseEntity<List<SphereDTO>> getAllSpheres(@ApiParam Pageable pageable, @Param("type") String type) {
        log.debug("REST request to get a page of Spheres");
        Page<SphereDTO> page = null;
        if ("USER".equals(type)) {
            page = sphereService.findSpheresPourUtilisateur(pageable);
        }
        else{
            page = sphereService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/spheres");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /spheres/:id : get the "id" sphere.
     *
     * @param id the id of the sphereDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sphereDTO, or with status 404 (Not Found)
     */
    @GetMapping("/spheres/{id}")
    @Timed
    public ResponseEntity<SphereDTO> getSphere(@PathVariable Long id) {
        log.debug("REST request to get Sphere : {}", id);
        SphereDTO sphereDTO = sphereService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sphereDTO));
    }

    /**
     * DELETE  /spheres/:id : delete the "id" sphere.
     *
     * @param id the id of the sphereDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/spheres/{id}")
    @Timed
    public ResponseEntity<Void> deleteSphere(@PathVariable Long id) {
        log.debug("REST request to delete Sphere : {}", id);

        SphereDTO sphereDTO = sphereService.findOne(id);

        if (userService.getUserWithAuthorities() != null && !userService.getUserWithAuthorities().getId().equals(sphereDTO.getAdministrateurId())) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sphere cannot already have an ID")).body(null);
        }

        sphereService.delete(id);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/spheres?query=:query : search for the sphere corresponding
     * to the query.
     *
     * @param query the query of the sphere search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/spheres")
    @Timed
    public ResponseEntity<List<SphereDTO>> searchSpheres(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Spheres for query {}", query);
        Page<SphereDTO> page = sphereService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/spheres");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
