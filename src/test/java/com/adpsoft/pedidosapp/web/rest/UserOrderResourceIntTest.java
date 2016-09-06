package com.adpsoft.pedidosapp.web.rest;

import com.adpsoft.pedidosapp.PedidosApp;
import com.adpsoft.pedidosapp.domain.UserOrder;
import com.adpsoft.pedidosapp.repository.UserOrderRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adpsoft.pedidosapp.domain.enumeration.OrderStatus;
/**
 * Test class for the UserOrderResource REST controller.
 *
 * @see UserOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PedidosApp.class)
public class UserOrderResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.PENDING;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.ORDERED;

    @Inject
    private UserOrderRepository userOrderRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUserOrderMockMvc;

    private UserOrder userOrder;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserOrderResource userOrderResource = new UserOrderResource();
        ReflectionTestUtils.setField(userOrderResource, "userOrderRepository", userOrderRepository);
        this.restUserOrderMockMvc = MockMvcBuilders.standaloneSetup(userOrderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserOrder createEntity(EntityManager em) {
        UserOrder userOrder = new UserOrder();
        userOrder = new UserOrder()
                .date(DEFAULT_DATE)
                .status(DEFAULT_STATUS);
        return userOrder;
    }

    @Before
    public void initTest() {
        userOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserOrder() throws Exception {
        int databaseSizeBeforeCreate = userOrderRepository.findAll().size();

        // Create the UserOrder

        restUserOrderMockMvc.perform(post("/api/user-orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userOrder)))
                .andExpect(status().isCreated());

        // Validate the UserOrder in the database
        List<UserOrder> userOrders = userOrderRepository.findAll();
        assertThat(userOrders).hasSize(databaseSizeBeforeCreate + 1);
        UserOrder testUserOrder = userOrders.get(userOrders.size() - 1);
        assertThat(testUserOrder.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testUserOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void getAllUserOrders() throws Exception {
        // Initialize the database
        userOrderRepository.saveAndFlush(userOrder);

        // Get all the userOrders
        restUserOrderMockMvc.perform(get("/api/user-orders?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userOrder.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getUserOrder() throws Exception {
        // Initialize the database
        userOrderRepository.saveAndFlush(userOrder);

        // Get the userOrder
        restUserOrderMockMvc.perform(get("/api/user-orders/{id}", userOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userOrder.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserOrder() throws Exception {
        // Get the userOrder
        restUserOrderMockMvc.perform(get("/api/user-orders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserOrder() throws Exception {
        // Initialize the database
        userOrderRepository.saveAndFlush(userOrder);
        int databaseSizeBeforeUpdate = userOrderRepository.findAll().size();

        // Update the userOrder
        UserOrder updatedUserOrder = userOrderRepository.findOne(userOrder.getId());
        updatedUserOrder
                .date(UPDATED_DATE)
                .status(UPDATED_STATUS);

        restUserOrderMockMvc.perform(put("/api/user-orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUserOrder)))
                .andExpect(status().isOk());

        // Validate the UserOrder in the database
        List<UserOrder> userOrders = userOrderRepository.findAll();
        assertThat(userOrders).hasSize(databaseSizeBeforeUpdate);
        UserOrder testUserOrder = userOrders.get(userOrders.size() - 1);
        assertThat(testUserOrder.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testUserOrder.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteUserOrder() throws Exception {
        // Initialize the database
        userOrderRepository.saveAndFlush(userOrder);
        int databaseSizeBeforeDelete = userOrderRepository.findAll().size();

        // Get the userOrder
        restUserOrderMockMvc.perform(delete("/api/user-orders/{id}", userOrder.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserOrder> userOrders = userOrderRepository.findAll();
        assertThat(userOrders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
