package com.cinema.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cinema.IntegrationTest;
import com.cinema.domain.Chair;
import com.cinema.domain.Moviefunction;
import com.cinema.repository.ChairRepository;
import com.cinema.service.dto.ChairDTO;
import com.cinema.service.mapper.ChairMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ChairResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChairResourceIT {

    private static final String DEFAULT_LOCATION = "AAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBB";

    private static final Boolean DEFAULT_AVAIBLE_CHAIR = false;
    private static final Boolean UPDATED_AVAIBLE_CHAIR = true;

    private static final String ENTITY_API_URL = "/api/chairs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChairRepository chairRepository;

    @Autowired
    private ChairMapper chairMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChairMockMvc;

    private Chair chair;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chair createEntity(EntityManager em) {
        Chair chair = new Chair().location(DEFAULT_LOCATION).avaible_chair(DEFAULT_AVAIBLE_CHAIR);
        // Add required entity
        Moviefunction moviefunction;
        if (TestUtil.findAll(em, Moviefunction.class).isEmpty()) {
            moviefunction = MoviefunctionResourceIT.createEntity(em);
            em.persist(moviefunction);
            em.flush();
        } else {
            moviefunction = TestUtil.findAll(em, Moviefunction.class).get(0);
        }
        chair.setMoviefunction(moviefunction);
        return chair;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chair createUpdatedEntity(EntityManager em) {
        Chair chair = new Chair().location(UPDATED_LOCATION).avaible_chair(UPDATED_AVAIBLE_CHAIR);
        // Add required entity
        Moviefunction moviefunction;
        if (TestUtil.findAll(em, Moviefunction.class).isEmpty()) {
            moviefunction = MoviefunctionResourceIT.createUpdatedEntity(em);
            em.persist(moviefunction);
            em.flush();
        } else {
            moviefunction = TestUtil.findAll(em, Moviefunction.class).get(0);
        }
        chair.setMoviefunction(moviefunction);
        return chair;
    }

    @BeforeEach
    public void initTest() {
        chair = createEntity(em);
    }

    @Test
    @Transactional
    void createChair() throws Exception {
        int databaseSizeBeforeCreate = chairRepository.findAll().size();
        // Create the Chair
        ChairDTO chairDTO = chairMapper.toDto(chair);
        restChairMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chairDTO)))
            .andExpect(status().isCreated());

        // Validate the Chair in the database
        List<Chair> chairList = chairRepository.findAll();
        assertThat(chairList).hasSize(databaseSizeBeforeCreate + 1);
        Chair testChair = chairList.get(chairList.size() - 1);
        assertThat(testChair.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testChair.getAvaible_chair()).isEqualTo(DEFAULT_AVAIBLE_CHAIR);
    }

    @Test
    @Transactional
    void createChairWithExistingId() throws Exception {
        // Create the Chair with an existing ID
        chair.setId(1L);
        ChairDTO chairDTO = chairMapper.toDto(chair);

        int databaseSizeBeforeCreate = chairRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChairMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chairDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Chair in the database
        List<Chair> chairList = chairRepository.findAll();
        assertThat(chairList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = chairRepository.findAll().size();
        // set the field null
        chair.setLocation(null);

        // Create the Chair, which fails.
        ChairDTO chairDTO = chairMapper.toDto(chair);

        restChairMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chairDTO)))
            .andExpect(status().isBadRequest());

        List<Chair> chairList = chairRepository.findAll();
        assertThat(chairList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvaible_chairIsRequired() throws Exception {
        int databaseSizeBeforeTest = chairRepository.findAll().size();
        // set the field null
        chair.setAvaible_chair(null);

        // Create the Chair, which fails.
        ChairDTO chairDTO = chairMapper.toDto(chair);

        restChairMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chairDTO)))
            .andExpect(status().isBadRequest());

        List<Chair> chairList = chairRepository.findAll();
        assertThat(chairList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChairs() throws Exception {
        // Initialize the database
        chairRepository.saveAndFlush(chair);

        // Get all the chairList
        restChairMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chair.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].avaible_chair").value(hasItem(DEFAULT_AVAIBLE_CHAIR.booleanValue())));
    }

    @Test
    @Transactional
    void getChair() throws Exception {
        // Initialize the database
        chairRepository.saveAndFlush(chair);

        // Get the chair
        restChairMockMvc
            .perform(get(ENTITY_API_URL_ID, chair.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chair.getId().intValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.avaible_chair").value(DEFAULT_AVAIBLE_CHAIR.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingChair() throws Exception {
        // Get the chair
        restChairMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChair() throws Exception {
        // Initialize the database
        chairRepository.saveAndFlush(chair);

        int databaseSizeBeforeUpdate = chairRepository.findAll().size();

        // Update the chair
        Chair updatedChair = chairRepository.findById(chair.getId()).get();
        // Disconnect from session so that the updates on updatedChair are not directly saved in db
        em.detach(updatedChair);
        updatedChair.location(UPDATED_LOCATION).avaible_chair(UPDATED_AVAIBLE_CHAIR);
        ChairDTO chairDTO = chairMapper.toDto(updatedChair);

        restChairMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chairDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chairDTO))
            )
            .andExpect(status().isOk());

        // Validate the Chair in the database
        List<Chair> chairList = chairRepository.findAll();
        assertThat(chairList).hasSize(databaseSizeBeforeUpdate);
        Chair testChair = chairList.get(chairList.size() - 1);
        assertThat(testChair.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testChair.getAvaible_chair()).isEqualTo(UPDATED_AVAIBLE_CHAIR);
    }

    @Test
    @Transactional
    void putNonExistingChair() throws Exception {
        int databaseSizeBeforeUpdate = chairRepository.findAll().size();
        chair.setId(count.incrementAndGet());

        // Create the Chair
        ChairDTO chairDTO = chairMapper.toDto(chair);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChairMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chairDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chairDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chair in the database
        List<Chair> chairList = chairRepository.findAll();
        assertThat(chairList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChair() throws Exception {
        int databaseSizeBeforeUpdate = chairRepository.findAll().size();
        chair.setId(count.incrementAndGet());

        // Create the Chair
        ChairDTO chairDTO = chairMapper.toDto(chair);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChairMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chairDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chair in the database
        List<Chair> chairList = chairRepository.findAll();
        assertThat(chairList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChair() throws Exception {
        int databaseSizeBeforeUpdate = chairRepository.findAll().size();
        chair.setId(count.incrementAndGet());

        // Create the Chair
        ChairDTO chairDTO = chairMapper.toDto(chair);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChairMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chairDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Chair in the database
        List<Chair> chairList = chairRepository.findAll();
        assertThat(chairList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChairWithPatch() throws Exception {
        // Initialize the database
        chairRepository.saveAndFlush(chair);

        int databaseSizeBeforeUpdate = chairRepository.findAll().size();

        // Update the chair using partial update
        Chair partialUpdatedChair = new Chair();
        partialUpdatedChair.setId(chair.getId());

        restChairMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChair.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChair))
            )
            .andExpect(status().isOk());

        // Validate the Chair in the database
        List<Chair> chairList = chairRepository.findAll();
        assertThat(chairList).hasSize(databaseSizeBeforeUpdate);
        Chair testChair = chairList.get(chairList.size() - 1);
        assertThat(testChair.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testChair.getAvaible_chair()).isEqualTo(DEFAULT_AVAIBLE_CHAIR);
    }

    @Test
    @Transactional
    void fullUpdateChairWithPatch() throws Exception {
        // Initialize the database
        chairRepository.saveAndFlush(chair);

        int databaseSizeBeforeUpdate = chairRepository.findAll().size();

        // Update the chair using partial update
        Chair partialUpdatedChair = new Chair();
        partialUpdatedChair.setId(chair.getId());

        partialUpdatedChair.location(UPDATED_LOCATION).avaible_chair(UPDATED_AVAIBLE_CHAIR);

        restChairMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChair.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChair))
            )
            .andExpect(status().isOk());

        // Validate the Chair in the database
        List<Chair> chairList = chairRepository.findAll();
        assertThat(chairList).hasSize(databaseSizeBeforeUpdate);
        Chair testChair = chairList.get(chairList.size() - 1);
        assertThat(testChair.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testChair.getAvaible_chair()).isEqualTo(UPDATED_AVAIBLE_CHAIR);
    }

    @Test
    @Transactional
    void patchNonExistingChair() throws Exception {
        int databaseSizeBeforeUpdate = chairRepository.findAll().size();
        chair.setId(count.incrementAndGet());

        // Create the Chair
        ChairDTO chairDTO = chairMapper.toDto(chair);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChairMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chairDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chairDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chair in the database
        List<Chair> chairList = chairRepository.findAll();
        assertThat(chairList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChair() throws Exception {
        int databaseSizeBeforeUpdate = chairRepository.findAll().size();
        chair.setId(count.incrementAndGet());

        // Create the Chair
        ChairDTO chairDTO = chairMapper.toDto(chair);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChairMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chairDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chair in the database
        List<Chair> chairList = chairRepository.findAll();
        assertThat(chairList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChair() throws Exception {
        int databaseSizeBeforeUpdate = chairRepository.findAll().size();
        chair.setId(count.incrementAndGet());

        // Create the Chair
        ChairDTO chairDTO = chairMapper.toDto(chair);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChairMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(chairDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Chair in the database
        List<Chair> chairList = chairRepository.findAll();
        assertThat(chairList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChair() throws Exception {
        // Initialize the database
        chairRepository.saveAndFlush(chair);

        int databaseSizeBeforeDelete = chairRepository.findAll().size();

        // Delete the chair
        restChairMockMvc
            .perform(delete(ENTITY_API_URL_ID, chair.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Chair> chairList = chairRepository.findAll();
        assertThat(chairList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
