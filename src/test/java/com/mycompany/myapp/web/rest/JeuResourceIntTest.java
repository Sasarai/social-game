package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SocialGameApp;

import com.mycompany.myapp.domain.Jeu;
import com.mycompany.myapp.repository.JeuRepository;
import com.mycompany.myapp.service.JeuService;
import com.mycompany.myapp.repository.search.JeuSearchRepository;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.service.dto.JeuDTO;
import com.mycompany.myapp.service.mapper.JeuMapper;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.GenreJeu;
/**
 * Test class for the JeuResource REST controller.
 *
 * @see JeuResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialGameApp.class)
public class JeuResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Double DEFAULT_NOMBRE_JOUEUR_MIN = 1D;
    private static final Double UPDATED_NOMBRE_JOUEUR_MIN = 2D;

    private static final Double DEFAULT_NOMBRE_JOUEUR_MAX = 1D;
    private static final Double UPDATED_NOMBRE_JOUEUR_MAX = 2D;

    private static final Double DEFAULT_DUREE_MOYENNE = 1D;
    private static final Double UPDATED_DUREE_MOYENNE = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final GenreJeu DEFAULT_GENRE = GenreJeu.COOPERATION;
    private static final GenreJeu UPDATED_GENRE = GenreJeu.CHACUN_POUR_SOI;

    @Autowired
    private JeuRepository jeuRepository;

    @Autowired
    private JeuMapper jeuMapper;

    @Autowired
    private JeuService jeuService;

    @Autowired
    private UserService userService;

    @Autowired
    private JeuSearchRepository jeuSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJeuMockMvc;

    private Jeu jeu;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JeuResource jeuResource = new JeuResource(jeuService, userService);
        this.restJeuMockMvc = MockMvcBuilders.standaloneSetup(jeuResource)
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
    public static Jeu createEntity(EntityManager em) {
        Jeu jeu = new Jeu()
            .nom(DEFAULT_NOM)
            .nombreJoueurMin(DEFAULT_NOMBRE_JOUEUR_MIN)
            .nombreJoueurMax(DEFAULT_NOMBRE_JOUEUR_MAX)
            .dureeMoyenne(DEFAULT_DUREE_MOYENNE)
            .description(DEFAULT_DESCRIPTION)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .genre(DEFAULT_GENRE);
        return jeu;
    }

    @Before
    public void initTest() {
        jeuSearchRepository.deleteAll();
        jeu = createEntity(em);
    }

    @Test
    @Transactional
    public void createJeu() throws Exception {
        int databaseSizeBeforeCreate = jeuRepository.findAll().size();

        // Create the Jeu
        JeuDTO jeuDTO = jeuMapper.toDto(jeu);
        restJeuMockMvc.perform(post("/api/jeus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jeuDTO)))
            .andExpect(status().isCreated());

        // Validate the Jeu in the database
        List<Jeu> jeuList = jeuRepository.findAll();
        assertThat(jeuList).hasSize(databaseSizeBeforeCreate + 1);
        Jeu testJeu = jeuList.get(jeuList.size() - 1);
        assertThat(testJeu.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testJeu.getNombreJoueurMin()).isEqualTo(DEFAULT_NOMBRE_JOUEUR_MIN);
        assertThat(testJeu.getNombreJoueurMax()).isEqualTo(DEFAULT_NOMBRE_JOUEUR_MAX);
        assertThat(testJeu.getDureeMoyenne()).isEqualTo(DEFAULT_DUREE_MOYENNE);
        assertThat(testJeu.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testJeu.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testJeu.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testJeu.getGenre()).isEqualTo(DEFAULT_GENRE);

        // Validate the Jeu in Elasticsearch
        Jeu jeuEs = jeuSearchRepository.findOne(testJeu.getId());
        assertThat(jeuEs).isEqualToComparingFieldByField(testJeu);
    }

    @Test
    @Transactional
    public void createJeuWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jeuRepository.findAll().size();

        // Create the Jeu with an existing ID
        jeu.setId(1L);
        JeuDTO jeuDTO = jeuMapper.toDto(jeu);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJeuMockMvc.perform(post("/api/jeus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jeuDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Jeu> jeuList = jeuRepository.findAll();
        assertThat(jeuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJeus() throws Exception {
        // Initialize the database
        jeuRepository.saveAndFlush(jeu);

        // Get all the jeuList
        restJeuMockMvc.perform(get("/api/jeus?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jeu.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].nombreJoueurMin").value(hasItem(DEFAULT_NOMBRE_JOUEUR_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].nombreJoueurMax").value(hasItem(DEFAULT_NOMBRE_JOUEUR_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].dureeMoyenne").value(hasItem(DEFAULT_DUREE_MOYENNE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE.toString())));
    }

    @Test
    @Transactional
    public void getJeu() throws Exception {
        // Initialize the database
        jeuRepository.saveAndFlush(jeu);

        // Get the jeu
        restJeuMockMvc.perform(get("/api/jeus/{id}", jeu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jeu.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.nombreJoueurMin").value(DEFAULT_NOMBRE_JOUEUR_MIN.doubleValue()))
            .andExpect(jsonPath("$.nombreJoueurMax").value(DEFAULT_NOMBRE_JOUEUR_MAX.doubleValue()))
            .andExpect(jsonPath("$.dureeMoyenne").value(DEFAULT_DUREE_MOYENNE.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.genre").value(DEFAULT_GENRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJeu() throws Exception {
        // Get the jeu
        restJeuMockMvc.perform(get("/api/jeus/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJeu() throws Exception {
        // Initialize the database
        jeuRepository.saveAndFlush(jeu);
        jeuSearchRepository.save(jeu);
        int databaseSizeBeforeUpdate = jeuRepository.findAll().size();

        // Update the jeu
        Jeu updatedJeu = jeuRepository.findOne(jeu.getId());
        updatedJeu
            .nom(UPDATED_NOM)
            .nombreJoueurMin(UPDATED_NOMBRE_JOUEUR_MIN)
            .nombreJoueurMax(UPDATED_NOMBRE_JOUEUR_MAX)
            .dureeMoyenne(UPDATED_DUREE_MOYENNE)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .genre(UPDATED_GENRE);
        JeuDTO jeuDTO = jeuMapper.toDto(updatedJeu);

        restJeuMockMvc.perform(put("/api/jeus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jeuDTO)))
            .andExpect(status().isOk());

        // Validate the Jeu in the database
        List<Jeu> jeuList = jeuRepository.findAll();
        assertThat(jeuList).hasSize(databaseSizeBeforeUpdate);
        Jeu testJeu = jeuList.get(jeuList.size() - 1);
        assertThat(testJeu.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testJeu.getNombreJoueurMin()).isEqualTo(UPDATED_NOMBRE_JOUEUR_MIN);
        assertThat(testJeu.getNombreJoueurMax()).isEqualTo(UPDATED_NOMBRE_JOUEUR_MAX);
        assertThat(testJeu.getDureeMoyenne()).isEqualTo(UPDATED_DUREE_MOYENNE);
        assertThat(testJeu.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testJeu.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testJeu.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testJeu.getGenre()).isEqualTo(UPDATED_GENRE);

        // Validate the Jeu in Elasticsearch
        Jeu jeuEs = jeuSearchRepository.findOne(testJeu.getId());
        assertThat(jeuEs).isEqualToComparingFieldByField(testJeu);
    }

    @Test
    @Transactional
    public void updateNonExistingJeu() throws Exception {
        int databaseSizeBeforeUpdate = jeuRepository.findAll().size();

        // Create the Jeu
        JeuDTO jeuDTO = jeuMapper.toDto(jeu);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJeuMockMvc.perform(put("/api/jeus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jeuDTO)))
            .andExpect(status().isCreated());

        // Validate the Jeu in the database
        List<Jeu> jeuList = jeuRepository.findAll();
        assertThat(jeuList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJeu() throws Exception {
        // Initialize the database
        jeuRepository.saveAndFlush(jeu);
        jeuSearchRepository.save(jeu);
        int databaseSizeBeforeDelete = jeuRepository.findAll().size();

        // Get the jeu
        restJeuMockMvc.perform(delete("/api/jeus/{id}", jeu.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean jeuExistsInEs = jeuSearchRepository.exists(jeu.getId());
        assertThat(jeuExistsInEs).isFalse();

        // Validate the database is empty
        List<Jeu> jeuList = jeuRepository.findAll();
        assertThat(jeuList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJeu() throws Exception {
        // Initialize the database
        jeuRepository.saveAndFlush(jeu);
        jeuSearchRepository.save(jeu);

        // Search the jeu
        restJeuMockMvc.perform(get("/api/_search/jeus?query=id:" + jeu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jeu.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].nombreJoueurMin").value(hasItem(DEFAULT_NOMBRE_JOUEUR_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].nombreJoueurMax").value(hasItem(DEFAULT_NOMBRE_JOUEUR_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].dureeMoyenne").value(hasItem(DEFAULT_DUREE_MOYENNE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Jeu.class);
        Jeu jeu1 = new Jeu();
        jeu1.setId(1L);
        Jeu jeu2 = new Jeu();
        jeu2.setId(jeu1.getId());
        assertThat(jeu1).isEqualTo(jeu2);
        jeu2.setId(2L);
        assertThat(jeu1).isNotEqualTo(jeu2);
        jeu1.setId(null);
        assertThat(jeu1).isNotEqualTo(jeu2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JeuDTO.class);
        JeuDTO jeuDTO1 = new JeuDTO();
        jeuDTO1.setId(1L);
        JeuDTO jeuDTO2 = new JeuDTO();
        assertThat(jeuDTO1).isNotEqualTo(jeuDTO2);
        jeuDTO2.setId(jeuDTO1.getId());
        assertThat(jeuDTO1).isEqualTo(jeuDTO2);
        jeuDTO2.setId(2L);
        assertThat(jeuDTO1).isNotEqualTo(jeuDTO2);
        jeuDTO1.setId(null);
        assertThat(jeuDTO1).isNotEqualTo(jeuDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(jeuMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(jeuMapper.fromId(null)).isNull();
    }
}
