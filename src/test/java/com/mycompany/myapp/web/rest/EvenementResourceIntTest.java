package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SocialGameApp;

import com.mycompany.myapp.domain.Evenement;
import com.mycompany.myapp.repository.EvenementRepository;
import com.mycompany.myapp.service.EvenementService;
import com.mycompany.myapp.repository.search.EvenementSearchRepository;
import com.mycompany.myapp.service.dto.EvenementDTO;
import com.mycompany.myapp.service.mapper.EvenementMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EvenementResource REST controller.
 *
 * @see EvenementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialGameApp.class)
public class EvenementResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LIEU = "AAAAAAAAAA";
    private static final String UPDATED_LIEU = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_FIN_VOTE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FIN_VOTE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private EvenementRepository evenementRepository;

    @Autowired
    private EvenementMapper evenementMapper;

    @Autowired
    private EvenementService evenementService;

    @Autowired
    private EvenementSearchRepository evenementSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEvenementMockMvc;

    private Evenement evenement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EvenementResource evenementResource = new EvenementResource(evenementService);
        this.restEvenementMockMvc = MockMvcBuilders.standaloneSetup(evenementResource)
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
    public static Evenement createEntity(EntityManager em) {
        Evenement evenement = new Evenement()
            .date(DEFAULT_DATE)
            .lieu(DEFAULT_LIEU)
            .nom(DEFAULT_NOM)
            .dateFinVote(DEFAULT_DATE_FIN_VOTE);
        return evenement;
    }

    @Before
    public void initTest() {
        evenementSearchRepository.deleteAll();
        evenement = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvenement() throws Exception {
        int databaseSizeBeforeCreate = evenementRepository.findAll().size();

        // Create the Evenement
        EvenementDTO evenementDTO = evenementMapper.toDto(evenement);
        restEvenementMockMvc.perform(post("/api/evenements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evenementDTO)))
            .andExpect(status().isCreated());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeCreate + 1);
        Evenement testEvenement = evenementList.get(evenementList.size() - 1);
        assertThat(testEvenement.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testEvenement.getLieu()).isEqualTo(DEFAULT_LIEU);
        assertThat(testEvenement.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEvenement.getDateFinVote()).isEqualTo(DEFAULT_DATE_FIN_VOTE);

        // Validate the Evenement in Elasticsearch
        Evenement evenementEs = evenementSearchRepository.findOne(testEvenement.getId());
        assertThat(evenementEs).isEqualToComparingFieldByField(testEvenement);
    }

    @Test
    @Transactional
    public void createEvenementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = evenementRepository.findAll().size();

        // Create the Evenement with an existing ID
        evenement.setId(1L);
        EvenementDTO evenementDTO = evenementMapper.toDto(evenement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvenementMockMvc.perform(post("/api/evenements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evenementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEvenements() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList
        restEvenementMockMvc.perform(get("/api/evenements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evenement.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].lieu").value(hasItem(DEFAULT_LIEU.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].dateFinVote").value(hasItem(sameInstant(DEFAULT_DATE_FIN_VOTE))));
    }

    @Test
    @Transactional
    public void getEvenement() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get the evenement
        restEvenementMockMvc.perform(get("/api/evenements/{id}", evenement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(evenement.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.lieu").value(DEFAULT_LIEU.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.dateFinVote").value(sameInstant(DEFAULT_DATE_FIN_VOTE)));
    }

    @Test
    @Transactional
    public void getNonExistingEvenement() throws Exception {
        // Get the evenement
        restEvenementMockMvc.perform(get("/api/evenements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvenement() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);
        evenementSearchRepository.save(evenement);
        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();

        // Update the evenement
        Evenement updatedEvenement = evenementRepository.findOne(evenement.getId());
        updatedEvenement
            .date(UPDATED_DATE)
            .lieu(UPDATED_LIEU)
            .nom(UPDATED_NOM)
            .dateFinVote(UPDATED_DATE_FIN_VOTE);
        EvenementDTO evenementDTO = evenementMapper.toDto(updatedEvenement);

        restEvenementMockMvc.perform(put("/api/evenements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evenementDTO)))
            .andExpect(status().isOk());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
        Evenement testEvenement = evenementList.get(evenementList.size() - 1);
        assertThat(testEvenement.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testEvenement.getLieu()).isEqualTo(UPDATED_LIEU);
        assertThat(testEvenement.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEvenement.getDateFinVote()).isEqualTo(UPDATED_DATE_FIN_VOTE);

        // Validate the Evenement in Elasticsearch
        Evenement evenementEs = evenementSearchRepository.findOne(testEvenement.getId());
        assertThat(evenementEs).isEqualToComparingFieldByField(testEvenement);
    }

    @Test
    @Transactional
    public void updateNonExistingEvenement() throws Exception {
        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();

        // Create the Evenement
        EvenementDTO evenementDTO = evenementMapper.toDto(evenement);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEvenementMockMvc.perform(put("/api/evenements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evenementDTO)))
            .andExpect(status().isCreated());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEvenement() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);
        evenementSearchRepository.save(evenement);
        int databaseSizeBeforeDelete = evenementRepository.findAll().size();

        // Get the evenement
        restEvenementMockMvc.perform(delete("/api/evenements/{id}", evenement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean evenementExistsInEs = evenementSearchRepository.exists(evenement.getId());
        assertThat(evenementExistsInEs).isFalse();

        // Validate the database is empty
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEvenement() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);
        evenementSearchRepository.save(evenement);

        // Search the evenement
        restEvenementMockMvc.perform(get("/api/_search/evenements?query=id:" + evenement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evenement.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].lieu").value(hasItem(DEFAULT_LIEU.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].dateFinVote").value(hasItem(sameInstant(DEFAULT_DATE_FIN_VOTE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evenement.class);
        Evenement evenement1 = new Evenement();
        evenement1.setId(1L);
        Evenement evenement2 = new Evenement();
        evenement2.setId(evenement1.getId());
        assertThat(evenement1).isEqualTo(evenement2);
        evenement2.setId(2L);
        assertThat(evenement1).isNotEqualTo(evenement2);
        evenement1.setId(null);
        assertThat(evenement1).isNotEqualTo(evenement2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EvenementDTO.class);
        EvenementDTO evenementDTO1 = new EvenementDTO();
        evenementDTO1.setId(1L);
        EvenementDTO evenementDTO2 = new EvenementDTO();
        assertThat(evenementDTO1).isNotEqualTo(evenementDTO2);
        evenementDTO2.setId(evenementDTO1.getId());
        assertThat(evenementDTO1).isEqualTo(evenementDTO2);
        evenementDTO2.setId(2L);
        assertThat(evenementDTO1).isNotEqualTo(evenementDTO2);
        evenementDTO1.setId(null);
        assertThat(evenementDTO1).isNotEqualTo(evenementDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(evenementMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(evenementMapper.fromId(null)).isNull();
    }
}
