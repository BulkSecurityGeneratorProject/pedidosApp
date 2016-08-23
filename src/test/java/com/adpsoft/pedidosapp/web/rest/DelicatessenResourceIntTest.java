package com.adpsoft.pedidosapp.web.rest;

import com.adpsoft.pedidosapp.PedidosApp;
import com.adpsoft.pedidosapp.domain.Delicatessen;
import com.adpsoft.pedidosapp.repository.DelicatessenRepository;

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
 * Test class for the DelicatessenResource REST controller.
 *
 * @see DelicatessenResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PedidosApp.class)
public class DelicatessenResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";
    private static final String DEFAULT_PHONE = "AAAAA";
    private static final String UPDATED_PHONE = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    private static final Boolean DEFAULT_CUSTOM = false;
    private static final Boolean UPDATED_CUSTOM = true;

    @Inject
    private DelicatessenRepository delicatessenRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDelicatessenMockMvc;

    private Delicatessen delicatessen;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DelicatessenResource delicatessenResource = new DelicatessenResource();
        ReflectionTestUtils.setField(delicatessenResource, "delicatessenRepository", delicatessenRepository);
        this.restDelicatessenMockMvc = MockMvcBuilders.standaloneSetup(delicatessenResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Delicatessen createEntity(EntityManager em) {
        Delicatessen delicatessen = new Delicatessen();
        delicatessen = new Delicatessen()
                .name(DEFAULT_NAME)
                .address(DEFAULT_ADDRESS)
                .phone(DEFAULT_PHONE)
                .email(DEFAULT_EMAIL)
                .custom(DEFAULT_CUSTOM);
        return delicatessen;
    }

    @Before
    public void initTest() {
        delicatessen = createEntity(em);
    }

    @Test
    @Transactional
    public void createDelicatessen() throws Exception {
        int databaseSizeBeforeCreate = delicatessenRepository.findAll().size();

        // Create the Delicatessen

        restDelicatessenMockMvc.perform(post("/api/delicatessens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(delicatessen)))
                .andExpect(status().isCreated());

        // Validate the Delicatessen in the database
        List<Delicatessen> delicatessens = delicatessenRepository.findAll();
        assertThat(delicatessens).hasSize(databaseSizeBeforeCreate + 1);
        Delicatessen testDelicatessen = delicatessens.get(delicatessens.size() - 1);
        assertThat(testDelicatessen.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDelicatessen.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testDelicatessen.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testDelicatessen.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testDelicatessen.isCustom()).isEqualTo(DEFAULT_CUSTOM);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = delicatessenRepository.findAll().size();
        // set the field null
        delicatessen.setName(null);

        // Create the Delicatessen, which fails.

        restDelicatessenMockMvc.perform(post("/api/delicatessens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(delicatessen)))
                .andExpect(status().isBadRequest());

        List<Delicatessen> delicatessens = delicatessenRepository.findAll();
        assertThat(delicatessens).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = delicatessenRepository.findAll().size();
        // set the field null
        delicatessen.setPhone(null);

        // Create the Delicatessen, which fails.

        restDelicatessenMockMvc.perform(post("/api/delicatessens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(delicatessen)))
                .andExpect(status().isBadRequest());

        List<Delicatessen> delicatessens = delicatessenRepository.findAll();
        assertThat(delicatessens).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = delicatessenRepository.findAll().size();
        // set the field null
        delicatessen.setEmail(null);

        // Create the Delicatessen, which fails.

        restDelicatessenMockMvc.perform(post("/api/delicatessens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(delicatessen)))
                .andExpect(status().isBadRequest());

        List<Delicatessen> delicatessens = delicatessenRepository.findAll();
        assertThat(delicatessens).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomIsRequired() throws Exception {
        int databaseSizeBeforeTest = delicatessenRepository.findAll().size();
        // set the field null
        delicatessen.setCustom(null);

        // Create the Delicatessen, which fails.

        restDelicatessenMockMvc.perform(post("/api/delicatessens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(delicatessen)))
                .andExpect(status().isBadRequest());

        List<Delicatessen> delicatessens = delicatessenRepository.findAll();
        assertThat(delicatessens).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDelicatessens() throws Exception {
        // Initialize the database
        delicatessenRepository.saveAndFlush(delicatessen);

        // Get all the delicatessens
        restDelicatessenMockMvc.perform(get("/api/delicatessens?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(delicatessen.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].custom").value(hasItem(DEFAULT_CUSTOM.booleanValue())));
    }

    @Test
    @Transactional
    public void getDelicatessen() throws Exception {
        // Initialize the database
        delicatessenRepository.saveAndFlush(delicatessen);

        // Get the delicatessen
        restDelicatessenMockMvc.perform(get("/api/delicatessens/{id}", delicatessen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(delicatessen.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.custom").value(DEFAULT_CUSTOM.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDelicatessen() throws Exception {
        // Get the delicatessen
        restDelicatessenMockMvc.perform(get("/api/delicatessens/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDelicatessen() throws Exception {
        // Initialize the database
        delicatessenRepository.saveAndFlush(delicatessen);
        int databaseSizeBeforeUpdate = delicatessenRepository.findAll().size();

        // Update the delicatessen
        Delicatessen updatedDelicatessen = delicatessenRepository.findOne(delicatessen.getId());
        updatedDelicatessen
                .name(UPDATED_NAME)
                .address(UPDATED_ADDRESS)
                .phone(UPDATED_PHONE)
                .email(UPDATED_EMAIL)
                .custom(UPDATED_CUSTOM);

        restDelicatessenMockMvc.perform(put("/api/delicatessens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDelicatessen)))
                .andExpect(status().isOk());

        // Validate the Delicatessen in the database
        List<Delicatessen> delicatessens = delicatessenRepository.findAll();
        assertThat(delicatessens).hasSize(databaseSizeBeforeUpdate);
        Delicatessen testDelicatessen = delicatessens.get(delicatessens.size() - 1);
        assertThat(testDelicatessen.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDelicatessen.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testDelicatessen.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testDelicatessen.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDelicatessen.isCustom()).isEqualTo(UPDATED_CUSTOM);
    }

    @Test
    @Transactional
    public void deleteDelicatessen() throws Exception {
        // Initialize the database
        delicatessenRepository.saveAndFlush(delicatessen);
        int databaseSizeBeforeDelete = delicatessenRepository.findAll().size();

        // Get the delicatessen
        restDelicatessenMockMvc.perform(delete("/api/delicatessens/{id}", delicatessen.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Delicatessen> delicatessens = delicatessenRepository.findAll();
        assertThat(delicatessens).hasSize(databaseSizeBeforeDelete - 1);
    }
}
