package com.adpsoft.pedidosapp.web.rest;

import com.adpsoft.pedidosapp.PedidosApp;
import com.adpsoft.pedidosapp.domain.Day;
import com.adpsoft.pedidosapp.repository.DayRepository;

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
 * Test class for the DayResource REST controller.
 *
 * @see DayResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PedidosApp.class)
public class DayResourceIntTest {
    private static final String DEFAULT_DAY = "AAAAA";
    private static final String UPDATED_DAY = "BBBBB";

    @Inject
    private DayRepository dayRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDayMockMvc;

    private Day day;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DayResource dayResource = new DayResource();
        ReflectionTestUtils.setField(dayResource, "dayRepository", dayRepository);
        this.restDayMockMvc = MockMvcBuilders.standaloneSetup(dayResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Day createEntity(EntityManager em) {
        Day day = new Day();
        day = new Day()
                .day(DEFAULT_DAY);
        return day;
    }

    @Before
    public void initTest() {
        day = createEntity(em);
    }

    @Test
    @Transactional
    public void createDay() throws Exception {
        int databaseSizeBeforeCreate = dayRepository.findAll().size();

        // Create the Day

        restDayMockMvc.perform(post("/api/days")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(day)))
                .andExpect(status().isCreated());

        // Validate the Day in the database
        List<Day> days = dayRepository.findAll();
        assertThat(days).hasSize(databaseSizeBeforeCreate + 1);
        Day testDay = days.get(days.size() - 1);
        assertThat(testDay.getDay()).isEqualTo(DEFAULT_DAY);
    }

    @Test
    @Transactional
    public void checkDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = dayRepository.findAll().size();
        // set the field null
        day.setDay(null);

        // Create the Day, which fails.

        restDayMockMvc.perform(post("/api/days")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(day)))
                .andExpect(status().isBadRequest());

        List<Day> days = dayRepository.findAll();
        assertThat(days).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDays() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);

        // Get all the days
        restDayMockMvc.perform(get("/api/days?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(day.getId().intValue())))
                .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY.toString())));
    }

    @Test
    @Transactional
    public void getDay() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);

        // Get the day
        restDayMockMvc.perform(get("/api/days/{id}", day.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(day.getId().intValue()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDay() throws Exception {
        // Get the day
        restDayMockMvc.perform(get("/api/days/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDay() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);
        int databaseSizeBeforeUpdate = dayRepository.findAll().size();

        // Update the day
        Day updatedDay = dayRepository.findOne(day.getId());
        updatedDay
                .day(UPDATED_DAY);

        restDayMockMvc.perform(put("/api/days")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDay)))
                .andExpect(status().isOk());

        // Validate the Day in the database
        List<Day> days = dayRepository.findAll();
        assertThat(days).hasSize(databaseSizeBeforeUpdate);
        Day testDay = days.get(days.size() - 1);
        assertThat(testDay.getDay()).isEqualTo(UPDATED_DAY);
    }

    @Test
    @Transactional
    public void deleteDay() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);
        int databaseSizeBeforeDelete = dayRepository.findAll().size();

        // Get the day
        restDayMockMvc.perform(delete("/api/days/{id}", day.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Day> days = dayRepository.findAll();
        assertThat(days).hasSize(databaseSizeBeforeDelete - 1);
    }
}
