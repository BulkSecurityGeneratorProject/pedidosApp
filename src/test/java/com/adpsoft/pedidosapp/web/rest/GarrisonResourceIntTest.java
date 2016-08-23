package com.adpsoft.pedidosapp.web.rest;

import com.adpsoft.pedidosapp.PedidosApp;
import com.adpsoft.pedidosapp.domain.Garrison;
import com.adpsoft.pedidosapp.repository.GarrisonRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GarrisonResource REST controller.
 *
 * @see GarrisonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PedidosApp.class)
public class GarrisonResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private GarrisonRepository garrisonRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGarrisonMockMvc;

    private Garrison garrison;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GarrisonResource garrisonResource = new GarrisonResource();
        ReflectionTestUtils.setField(garrisonResource, "garrisonRepository", garrisonRepository);
        this.restGarrisonMockMvc = MockMvcBuilders.standaloneSetup(garrisonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Garrison createEntity(EntityManager em) {
        Garrison garrison = new Garrison();
        garrison = new Garrison()
                .name(DEFAULT_NAME);
        return garrison;
    }

    @Before
    public void initTest() {
        garrison = createEntity(em);
    }

    @Test
    @Transactional
    public void createGarrison() throws Exception {
        int databaseSizeBeforeCreate = garrisonRepository.findAll().size();

        // Create the Garrison

        restGarrisonMockMvc.perform(post("/api/garrisons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(garrison)))
                .andExpect(status().isCreated());

        // Validate the Garrison in the database
        List<Garrison> garrisons = garrisonRepository.findAll();
        assertThat(garrisons).hasSize(databaseSizeBeforeCreate + 1);
        Garrison testGarrison = garrisons.get(garrisons.size() - 1);
        assertThat(testGarrison.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = garrisonRepository.findAll().size();
        // set the field null
        garrison.setName(null);

        // Create the Garrison, which fails.

        restGarrisonMockMvc.perform(post("/api/garrisons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(garrison)))
                .andExpect(status().isBadRequest());

        List<Garrison> garrisons = garrisonRepository.findAll();
        assertThat(garrisons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGarrisons() throws Exception {
        // Initialize the database
        garrisonRepository.saveAndFlush(garrison);

        // Get all the garrisons
        restGarrisonMockMvc.perform(get("/api/garrisons?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(garrison.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getGarrison() throws Exception {
        // Initialize the database
        garrisonRepository.saveAndFlush(garrison);

        // Get the garrison
        restGarrisonMockMvc.perform(get("/api/garrisons/{id}", garrison.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(garrison.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGarrison() throws Exception {
        // Get the garrison
        restGarrisonMockMvc.perform(get("/api/garrisons/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGarrison() throws Exception {
        // Initialize the database
        garrisonRepository.saveAndFlush(garrison);
        int databaseSizeBeforeUpdate = garrisonRepository.findAll().size();

        // Update the garrison
        Garrison updatedGarrison = garrisonRepository.findOne(garrison.getId());
        updatedGarrison
                .name(UPDATED_NAME);

        restGarrisonMockMvc.perform(put("/api/garrisons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGarrison)))
                .andExpect(status().isOk());

        // Validate the Garrison in the database
        List<Garrison> garrisons = garrisonRepository.findAll();
        assertThat(garrisons).hasSize(databaseSizeBeforeUpdate);
        Garrison testGarrison = garrisons.get(garrisons.size() - 1);
        assertThat(testGarrison.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteGarrison() throws Exception {
        // Initialize the database
        garrisonRepository.saveAndFlush(garrison);
        int databaseSizeBeforeDelete = garrisonRepository.findAll().size();

        // Get the garrison
        restGarrisonMockMvc.perform(delete("/api/garrisons/{id}", garrison.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Garrison> garrisons = garrisonRepository.findAll();
        assertThat(garrisons).hasSize(databaseSizeBeforeDelete - 1);
    }
}
