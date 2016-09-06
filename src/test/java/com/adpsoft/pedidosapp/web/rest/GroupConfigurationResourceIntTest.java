package com.adpsoft.pedidosapp.web.rest;

import com.adpsoft.pedidosapp.PedidosApp;
import com.adpsoft.pedidosapp.domain.GroupConfiguration;
import com.adpsoft.pedidosapp.repository.GroupConfigurationRepository;

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
 * Test class for the GroupConfigurationResource REST controller.
 *
 * @see GroupConfigurationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PedidosApp.class)
public class GroupConfigurationResourceIntTest {

    private static final Boolean DEFAULT_WEEKLY = false;
    private static final Boolean UPDATED_WEEKLY = true;

    private static final Integer DEFAULT_ORDER_OPENING_HOUR = 0;
    private static final Integer UPDATED_ORDER_OPENING_HOUR = 1;

    private static final Integer DEFAULT_ORDER_CLOSING_HOUR = 0;
    private static final Integer UPDATED_ORDER_CLOSING_HOUR = 1;
    private static final String DEFAULT_CC_ORDER_EMAIL = "AAAAA";
    private static final String UPDATED_CC_ORDER_EMAIL = "BBBBB";
    private static final String DEFAULT_CC_CANCEL_EMAIL = "AAAAA";
    private static final String UPDATED_CC_CANCEL_EMAIL = "BBBBB";
    private static final String DEFAULT_ORDER_EMAIL_BODY = "AAAAA";
    private static final String UPDATED_ORDER_EMAIL_BODY = "BBBBB";
    private static final String DEFAULT_CANCEL_EMAIL_BODY = "AAAAA";
    private static final String UPDATED_CANCEL_EMAIL_BODY = "BBBBB";

    private static final Integer DEFAULT_DAYS_FOR_CANCELLATION = 1;
    private static final Integer UPDATED_DAYS_FOR_CANCELLATION = 2;

    private static final Integer DEFAULT_HOURS_FOR_CANCELATION = 0;
    private static final Integer UPDATED_HOURS_FOR_CANCELATION = 1;

    @Inject
    private GroupConfigurationRepository groupConfigurationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGroupConfigurationMockMvc;

    private GroupConfiguration groupConfiguration;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GroupConfigurationResource groupConfigurationResource = new GroupConfigurationResource();
        ReflectionTestUtils.setField(groupConfigurationResource, "groupConfigurationRepository", groupConfigurationRepository);
        this.restGroupConfigurationMockMvc = MockMvcBuilders.standaloneSetup(groupConfigurationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupConfiguration createEntity(EntityManager em) {
        GroupConfiguration groupConfiguration = new GroupConfiguration();
        groupConfiguration = new GroupConfiguration()
                .weekly(DEFAULT_WEEKLY)
                .orderOpeningHour(DEFAULT_ORDER_OPENING_HOUR)
                .orderClosingHour(DEFAULT_ORDER_CLOSING_HOUR)
                .ccOrderEmail(DEFAULT_CC_ORDER_EMAIL)
                .ccCancelEmail(DEFAULT_CC_CANCEL_EMAIL)
                .orderEmailBody(DEFAULT_ORDER_EMAIL_BODY)
                .cancelEmailBody(DEFAULT_CANCEL_EMAIL_BODY)
                .daysForCancellation(DEFAULT_DAYS_FOR_CANCELLATION)
                .hoursForCancelation(DEFAULT_HOURS_FOR_CANCELATION);
        return groupConfiguration;
    }

    @Before
    public void initTest() {
        groupConfiguration = createEntity(em);
    }

    @Test
    @Transactional
    public void createGroupConfiguration() throws Exception {
        int databaseSizeBeforeCreate = groupConfigurationRepository.findAll().size();

        // Create the GroupConfiguration

        restGroupConfigurationMockMvc.perform(post("/api/group-configurations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(groupConfiguration)))
                .andExpect(status().isCreated());

        // Validate the GroupConfiguration in the database
        List<GroupConfiguration> groupConfigurations = groupConfigurationRepository.findAll();
        assertThat(groupConfigurations).hasSize(databaseSizeBeforeCreate + 1);
        GroupConfiguration testGroupConfiguration = groupConfigurations.get(groupConfigurations.size() - 1);
        assertThat(testGroupConfiguration.isWeekly()).isEqualTo(DEFAULT_WEEKLY);
        assertThat(testGroupConfiguration.getOrderOpeningHour()).isEqualTo(DEFAULT_ORDER_OPENING_HOUR);
        assertThat(testGroupConfiguration.getOrderClosingHour()).isEqualTo(DEFAULT_ORDER_CLOSING_HOUR);
        assertThat(testGroupConfiguration.getCcOrderEmail()).isEqualTo(DEFAULT_CC_ORDER_EMAIL);
        assertThat(testGroupConfiguration.getCcCancelEmail()).isEqualTo(DEFAULT_CC_CANCEL_EMAIL);
        assertThat(testGroupConfiguration.getOrderEmailBody()).isEqualTo(DEFAULT_ORDER_EMAIL_BODY);
        assertThat(testGroupConfiguration.getCancelEmailBody()).isEqualTo(DEFAULT_CANCEL_EMAIL_BODY);
        assertThat(testGroupConfiguration.getDaysForCancellation()).isEqualTo(DEFAULT_DAYS_FOR_CANCELLATION);
        assertThat(testGroupConfiguration.getHoursForCancelation()).isEqualTo(DEFAULT_HOURS_FOR_CANCELATION);
    }

    @Test
    @Transactional
    public void getAllGroupConfigurations() throws Exception {
        // Initialize the database
        groupConfigurationRepository.saveAndFlush(groupConfiguration);

        // Get all the groupConfigurations
        restGroupConfigurationMockMvc.perform(get("/api/group-configurations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(groupConfiguration.getId().intValue())))
                .andExpect(jsonPath("$.[*].weekly").value(hasItem(DEFAULT_WEEKLY.booleanValue())))
                .andExpect(jsonPath("$.[*].orderOpeningHour").value(hasItem(DEFAULT_ORDER_OPENING_HOUR)))
                .andExpect(jsonPath("$.[*].orderClosingHour").value(hasItem(DEFAULT_ORDER_CLOSING_HOUR)))
                .andExpect(jsonPath("$.[*].ccOrderEmail").value(hasItem(DEFAULT_CC_ORDER_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].ccCancelEmail").value(hasItem(DEFAULT_CC_CANCEL_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].orderEmailBody").value(hasItem(DEFAULT_ORDER_EMAIL_BODY.toString())))
                .andExpect(jsonPath("$.[*].cancelEmailBody").value(hasItem(DEFAULT_CANCEL_EMAIL_BODY.toString())))
                .andExpect(jsonPath("$.[*].daysForCancellation").value(hasItem(DEFAULT_DAYS_FOR_CANCELLATION)))
                .andExpect(jsonPath("$.[*].hoursForCancelation").value(hasItem(DEFAULT_HOURS_FOR_CANCELATION)));
    }

    @Test
    @Transactional
    public void getGroupConfiguration() throws Exception {
        // Initialize the database
        groupConfigurationRepository.saveAndFlush(groupConfiguration);

        // Get the groupConfiguration
        restGroupConfigurationMockMvc.perform(get("/api/group-configurations/{id}", groupConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(groupConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.weekly").value(DEFAULT_WEEKLY.booleanValue()))
            .andExpect(jsonPath("$.orderOpeningHour").value(DEFAULT_ORDER_OPENING_HOUR))
            .andExpect(jsonPath("$.orderClosingHour").value(DEFAULT_ORDER_CLOSING_HOUR))
            .andExpect(jsonPath("$.ccOrderEmail").value(DEFAULT_CC_ORDER_EMAIL.toString()))
            .andExpect(jsonPath("$.ccCancelEmail").value(DEFAULT_CC_CANCEL_EMAIL.toString()))
            .andExpect(jsonPath("$.orderEmailBody").value(DEFAULT_ORDER_EMAIL_BODY.toString()))
            .andExpect(jsonPath("$.cancelEmailBody").value(DEFAULT_CANCEL_EMAIL_BODY.toString()))
            .andExpect(jsonPath("$.daysForCancellation").value(DEFAULT_DAYS_FOR_CANCELLATION))
            .andExpect(jsonPath("$.hoursForCancelation").value(DEFAULT_HOURS_FOR_CANCELATION));
    }

    @Test
    @Transactional
    public void getNonExistingGroupConfiguration() throws Exception {
        // Get the groupConfiguration
        restGroupConfigurationMockMvc.perform(get("/api/group-configurations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGroupConfiguration() throws Exception {
        // Initialize the database
        groupConfigurationRepository.saveAndFlush(groupConfiguration);
        int databaseSizeBeforeUpdate = groupConfigurationRepository.findAll().size();

        // Update the groupConfiguration
        GroupConfiguration updatedGroupConfiguration = groupConfigurationRepository.findOne(groupConfiguration.getId());
        updatedGroupConfiguration
                .weekly(UPDATED_WEEKLY)
                .orderOpeningHour(UPDATED_ORDER_OPENING_HOUR)
                .orderClosingHour(UPDATED_ORDER_CLOSING_HOUR)
                .ccOrderEmail(UPDATED_CC_ORDER_EMAIL)
                .ccCancelEmail(UPDATED_CC_CANCEL_EMAIL)
                .orderEmailBody(UPDATED_ORDER_EMAIL_BODY)
                .cancelEmailBody(UPDATED_CANCEL_EMAIL_BODY)
                .daysForCancellation(UPDATED_DAYS_FOR_CANCELLATION)
                .hoursForCancelation(UPDATED_HOURS_FOR_CANCELATION);

        restGroupConfigurationMockMvc.perform(put("/api/group-configurations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGroupConfiguration)))
                .andExpect(status().isOk());

        // Validate the GroupConfiguration in the database
        List<GroupConfiguration> groupConfigurations = groupConfigurationRepository.findAll();
        assertThat(groupConfigurations).hasSize(databaseSizeBeforeUpdate);
        GroupConfiguration testGroupConfiguration = groupConfigurations.get(groupConfigurations.size() - 1);
        assertThat(testGroupConfiguration.isWeekly()).isEqualTo(UPDATED_WEEKLY);
        assertThat(testGroupConfiguration.getOrderOpeningHour()).isEqualTo(UPDATED_ORDER_OPENING_HOUR);
        assertThat(testGroupConfiguration.getOrderClosingHour()).isEqualTo(UPDATED_ORDER_CLOSING_HOUR);
        assertThat(testGroupConfiguration.getCcOrderEmail()).isEqualTo(UPDATED_CC_ORDER_EMAIL);
        assertThat(testGroupConfiguration.getCcCancelEmail()).isEqualTo(UPDATED_CC_CANCEL_EMAIL);
        assertThat(testGroupConfiguration.getOrderEmailBody()).isEqualTo(UPDATED_ORDER_EMAIL_BODY);
        assertThat(testGroupConfiguration.getCancelEmailBody()).isEqualTo(UPDATED_CANCEL_EMAIL_BODY);
        assertThat(testGroupConfiguration.getDaysForCancellation()).isEqualTo(UPDATED_DAYS_FOR_CANCELLATION);
        assertThat(testGroupConfiguration.getHoursForCancelation()).isEqualTo(UPDATED_HOURS_FOR_CANCELATION);
    }

    @Test
    @Transactional
    public void deleteGroupConfiguration() throws Exception {
        // Initialize the database
        groupConfigurationRepository.saveAndFlush(groupConfiguration);
        int databaseSizeBeforeDelete = groupConfigurationRepository.findAll().size();

        // Get the groupConfiguration
        restGroupConfigurationMockMvc.perform(delete("/api/group-configurations/{id}", groupConfiguration.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<GroupConfiguration> groupConfigurations = groupConfigurationRepository.findAll();
        assertThat(groupConfigurations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
