package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SocialGameApp;

import com.mycompany.myapp.domain.Sphere;
import com.mycompany.myapp.repository.SphereRepository;
import com.mycompany.myapp.service.SphereService;
import com.mycompany.myapp.repository.search.SphereSearchRepository;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.service.dto.SphereDTO;
import com.mycompany.myapp.service.mapper.SphereMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SphereResource REST controller.
 *
 * @see SphereResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialGameApp.class)
public class SphereResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    @Autowired
    private SphereRepository sphereRepository;

    @Autowired
    private SphereMapper sphereMapper;

    @Autowired
    private SphereService sphereService;

    @Autowired
    private UserService userService;

    @Autowired
    private SphereSearchRepository sphereSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSphereMockMvc;

    private Sphere sphere;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SphereResource sphereResource = new SphereResource(sphereService, userService);
        this.restSphereMockMvc = MockMvcBuilders.standaloneSetup(sphereResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sphere createEntity(EntityManager em) {
        Sphere sphere = new Sphere()
            .nom(DEFAULT_NOM);
        return sphere;
    }

    @Before
    public void initTest() {
        sphereSearchRepository.deleteAll();
        sphere = createEntity(em);
    }

    @Test
    @Transactional
    public void createSphere() throws Exception {
        int databaseSizeBeforeCreate = sphereRepository.findAll().size();

        // Create the Sphere
        SphereDTO sphereDTO = sphereMapper.toDto(sphere);
        restSphereMockMvc.perform(post("/api/spheres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sphereDTO)))
            .andExpect(status().isCreated());

        // Validate the Sphere in the database
        List<Sphere> sphereList = sphereRepository.findAll();
        assertThat(sphereList).hasSize(databaseSizeBeforeCreate + 1);
        Sphere testSphere = sphereList.get(sphereList.size() - 1);
        assertThat(testSphere.getNom()).isEqualTo(DEFAULT_NOM);

        // Validate the Sphere in Elasticsearch
        Sphere sphereEs = sphereSearchRepository.findOne(testSphere.getId());
        assertThat(sphereEs).isEqualToComparingFieldByField(testSphere);
    }

    @Test
    @Transactional
    public void createSphereWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sphereRepository.findAll().size();

        // Create the Sphere with an existing ID
        sphere.setId(1L);
        SphereDTO sphereDTO = sphereMapper.toDto(sphere);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSphereMockMvc.perform(post("/api/spheres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sphereDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Sphere> sphereList = sphereRepository.findAll();
        assertThat(sphereList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSpheres() throws Exception {
        // Initialize the database
        sphereRepository.saveAndFlush(sphere);

        // Get all the sphereList
        restSphereMockMvc.perform(get("/api/spheres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sphere.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void getSphere() throws Exception {
        // Initialize the database
        sphereRepository.saveAndFlush(sphere);

        // Get the sphere
        restSphereMockMvc.perform(get("/api/spheres/{id}", sphere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sphere.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSphere() throws Exception {
        // Get the sphere
        restSphereMockMvc.perform(get("/api/spheres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSphere() throws Exception {
        // Initialize the database
        sphereRepository.saveAndFlush(sphere);
        sphereSearchRepository.save(sphere);
        int databaseSizeBeforeUpdate = sphereRepository.findAll().size();

        // Update the sphere
        Sphere updatedSphere = sphereRepository.findOne(sphere.getId());
        updatedSphere
            .nom(UPDATED_NOM);
        SphereDTO sphereDTO = sphereMapper.toDto(updatedSphere);

        restSphereMockMvc.perform(put("/api/spheres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sphereDTO)))
            .andExpect(status().isOk());

        // Validate the Sphere in the database
        List<Sphere> sphereList = sphereRepository.findAll();
        assertThat(sphereList).hasSize(databaseSizeBeforeUpdate);
        Sphere testSphere = sphereList.get(sphereList.size() - 1);
        assertThat(testSphere.getNom()).isEqualTo(UPDATED_NOM);

        // Validate the Sphere in Elasticsearch
        Sphere sphereEs = sphereSearchRepository.findOne(testSphere.getId());
        assertThat(sphereEs).isEqualToComparingFieldByField(testSphere);
    }

    @Test
    @Transactional
    public void updateNonExistingSphere() throws Exception {
        int databaseSizeBeforeUpdate = sphereRepository.findAll().size();

        // Create the Sphere
        SphereDTO sphereDTO = sphereMapper.toDto(sphere);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSphereMockMvc.perform(put("/api/spheres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sphereDTO)))
            .andExpect(status().isCreated());

        // Validate the Sphere in the database
        List<Sphere> sphereList = sphereRepository.findAll();
        assertThat(sphereList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSphere() throws Exception {
        // Initialize the database
        sphereRepository.saveAndFlush(sphere);
        sphereSearchRepository.save(sphere);
        int databaseSizeBeforeDelete = sphereRepository.findAll().size();

        // Get the sphere
        restSphereMockMvc.perform(delete("/api/spheres/{id}", sphere.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean sphereExistsInEs = sphereSearchRepository.exists(sphere.getId());
        assertThat(sphereExistsInEs).isFalse();

        // Validate the database is empty
        List<Sphere> sphereList = sphereRepository.findAll();
        assertThat(sphereList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSphere() throws Exception {
        // Initialize the database
        sphereRepository.saveAndFlush(sphere);
        sphereSearchRepository.save(sphere);

        // Search the sphere
        restSphereMockMvc.perform(get("/api/_search/spheres?query=id:" + sphere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sphere.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sphere.class);
        Sphere sphere1 = new Sphere();
        sphere1.setId(1L);
        Sphere sphere2 = new Sphere();
        sphere2.setId(sphere1.getId());
        assertThat(sphere1).isEqualTo(sphere2);
        sphere2.setId(2L);
        assertThat(sphere1).isNotEqualTo(sphere2);
        sphere1.setId(null);
        assertThat(sphere1).isNotEqualTo(sphere2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SphereDTO.class);
        SphereDTO sphereDTO1 = new SphereDTO();
        sphereDTO1.setId(1L);
        SphereDTO sphereDTO2 = new SphereDTO();
        assertThat(sphereDTO1).isNotEqualTo(sphereDTO2);
        sphereDTO2.setId(sphereDTO1.getId());
        assertThat(sphereDTO1).isEqualTo(sphereDTO2);
        sphereDTO2.setId(2L);
        assertThat(sphereDTO1).isNotEqualTo(sphereDTO2);
        sphereDTO1.setId(null);
        assertThat(sphereDTO1).isNotEqualTo(sphereDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sphereMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sphereMapper.fromId(null)).isNull();
    }
}
