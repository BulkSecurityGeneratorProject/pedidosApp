package com.adpsoft.pedidosapp.web.rest;

import com.adpsoft.pedidosapp.PedidosApp;
import com.adpsoft.pedidosapp.domain.Invite;
import com.adpsoft.pedidosapp.repository.InviteRepository;

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

import com.adpsoft.pedidosapp.domain.enumeration.InviteStatus;
/**
 * Test class for the InviteResource REST controller.
 *
 * @see InviteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PedidosApp.class)
public class InviteResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));
    private static final String DEFAULT_GUEST_MAIL = "AAAAA";
    private static final String UPDATED_GUEST_MAIL = "BBBBB";
    private static final String DEFAULT_GUEST_NAME = "AAAAA";
    private static final String UPDATED_GUEST_NAME = "BBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);

    private static final InviteStatus DEFAULT_STATUS = InviteStatus.PENDING;
    private static final InviteStatus UPDATED_STATUS = InviteStatus.SENT;

    @Inject
    private InviteRepository inviteRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restInviteMockMvc;

    private Invite invite;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InviteResource inviteResource = new InviteResource();
        ReflectionTestUtils.setField(inviteResource, "inviteRepository", inviteRepository);
        this.restInviteMockMvc = MockMvcBuilders.standaloneSetup(inviteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invite createEntity(EntityManager em) {
        Invite invite = new Invite();
        invite = new Invite()
                .guestMail(DEFAULT_GUEST_MAIL)
                .guestName(DEFAULT_GUEST_NAME)
                .date(DEFAULT_DATE)
                .status(DEFAULT_STATUS);
        return invite;
    }

    @Before
    public void initTest() {
        invite = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvite() throws Exception {
        int databaseSizeBeforeCreate = inviteRepository.findAll().size();

        // Create the Invite

        restInviteMockMvc.perform(post("/api/invites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invite)))
                .andExpect(status().isCreated());

        // Validate the Invite in the database
        List<Invite> invites = inviteRepository.findAll();
        assertThat(invites).hasSize(databaseSizeBeforeCreate + 1);
        Invite testInvite = invites.get(invites.size() - 1);
        assertThat(testInvite.getGuestMail()).isEqualTo(DEFAULT_GUEST_MAIL);
        assertThat(testInvite.getGuestName()).isEqualTo(DEFAULT_GUEST_NAME);
        assertThat(testInvite.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testInvite.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkGuestMailIsRequired() throws Exception {
        int databaseSizeBeforeTest = inviteRepository.findAll().size();
        // set the field null
        invite.setGuestMail(null);

        // Create the Invite, which fails.

        restInviteMockMvc.perform(post("/api/invites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invite)))
                .andExpect(status().isBadRequest());

        List<Invite> invites = inviteRepository.findAll();
        assertThat(invites).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvites() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the invites
        restInviteMockMvc.perform(get("/api/invites?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(invite.getId().intValue())))
                .andExpect(jsonPath("$.[*].guestMail").value(hasItem(DEFAULT_GUEST_MAIL.toString())))
                .andExpect(jsonPath("$.[*].guestName").value(hasItem(DEFAULT_GUEST_NAME.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getInvite() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get the invite
        restInviteMockMvc.perform(get("/api/invites/{id}", invite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invite.getId().intValue()))
            .andExpect(jsonPath("$.guestMail").value(DEFAULT_GUEST_MAIL.toString()))
            .andExpect(jsonPath("$.guestName").value(DEFAULT_GUEST_NAME.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInvite() throws Exception {
        // Get the invite
        restInviteMockMvc.perform(get("/api/invites/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvite() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);
        int databaseSizeBeforeUpdate = inviteRepository.findAll().size();

        // Update the invite
        Invite updatedInvite = inviteRepository.findOne(invite.getId());
        updatedInvite
                .guestMail(UPDATED_GUEST_MAIL)
                .guestName(UPDATED_GUEST_NAME)
                .date(UPDATED_DATE)
                .status(UPDATED_STATUS);

        restInviteMockMvc.perform(put("/api/invites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInvite)))
                .andExpect(status().isOk());

        // Validate the Invite in the database
        List<Invite> invites = inviteRepository.findAll();
        assertThat(invites).hasSize(databaseSizeBeforeUpdate);
        Invite testInvite = invites.get(invites.size() - 1);
        assertThat(testInvite.getGuestMail()).isEqualTo(UPDATED_GUEST_MAIL);
        assertThat(testInvite.getGuestName()).isEqualTo(UPDATED_GUEST_NAME);
        assertThat(testInvite.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testInvite.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteInvite() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);
        int databaseSizeBeforeDelete = inviteRepository.findAll().size();

        // Get the invite
        restInviteMockMvc.perform(delete("/api/invites/{id}", invite.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Invite> invites = inviteRepository.findAll();
        assertThat(invites).hasSize(databaseSizeBeforeDelete - 1);
    }
}
