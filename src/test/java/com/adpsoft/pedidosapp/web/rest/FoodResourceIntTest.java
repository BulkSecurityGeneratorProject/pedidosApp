package com.adpsoft.pedidosapp.web.rest;

import com.adpsoft.pedidosapp.PedidosApp;
import com.adpsoft.pedidosapp.domain.Food;
import com.adpsoft.pedidosapp.repository.FoodRepository;

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
 * Test class for the FoodResource REST controller.
 *
 * @see FoodResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PedidosApp.class)
public class FoodResourceIntTest {
    private static final String DEFAULT_PREFIX = "AAAAA";
    private static final String UPDATED_PREFIX = "BBBBB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private FoodRepository foodRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFoodMockMvc;

    private Food food;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FoodResource foodResource = new FoodResource();
        ReflectionTestUtils.setField(foodResource, "foodRepository", foodRepository);
        this.restFoodMockMvc = MockMvcBuilders.standaloneSetup(foodResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Food createEntity(EntityManager em) {
        Food food = new Food();
        food = new Food()
                .prefix(DEFAULT_PREFIX)
                .name(DEFAULT_NAME);
        return food;
    }

    @Before
    public void initTest() {
        food = createEntity(em);
    }

    @Test
    @Transactional
    public void createFood() throws Exception {
        int databaseSizeBeforeCreate = foodRepository.findAll().size();

        // Create the Food

        restFoodMockMvc.perform(post("/api/foods")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(food)))
                .andExpect(status().isCreated());

        // Validate the Food in the database
        List<Food> foods = foodRepository.findAll();
        assertThat(foods).hasSize(databaseSizeBeforeCreate + 1);
        Food testFood = foods.get(foods.size() - 1);
        assertThat(testFood.getPrefix()).isEqualTo(DEFAULT_PREFIX);
        assertThat(testFood.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = foodRepository.findAll().size();
        // set the field null
        food.setName(null);

        // Create the Food, which fails.

        restFoodMockMvc.perform(post("/api/foods")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(food)))
                .andExpect(status().isBadRequest());

        List<Food> foods = foodRepository.findAll();
        assertThat(foods).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFoods() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get all the foods
        restFoodMockMvc.perform(get("/api/foods?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(food.getId().intValue())))
                .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getFood() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get the food
        restFoodMockMvc.perform(get("/api/foods/{id}", food.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(food.getId().intValue()))
            .andExpect(jsonPath("$.prefix").value(DEFAULT_PREFIX.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFood() throws Exception {
        // Get the food
        restFoodMockMvc.perform(get("/api/foods/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFood() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);
        int databaseSizeBeforeUpdate = foodRepository.findAll().size();

        // Update the food
        Food updatedFood = foodRepository.findOne(food.getId());
        updatedFood
                .prefix(UPDATED_PREFIX)
                .name(UPDATED_NAME);

        restFoodMockMvc.perform(put("/api/foods")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFood)))
                .andExpect(status().isOk());

        // Validate the Food in the database
        List<Food> foods = foodRepository.findAll();
        assertThat(foods).hasSize(databaseSizeBeforeUpdate);
        Food testFood = foods.get(foods.size() - 1);
        assertThat(testFood.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testFood.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteFood() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);
        int databaseSizeBeforeDelete = foodRepository.findAll().size();

        // Get the food
        restFoodMockMvc.perform(delete("/api/foods/{id}", food.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Food> foods = foodRepository.findAll();
        assertThat(foods).hasSize(databaseSizeBeforeDelete - 1);
    }
}
