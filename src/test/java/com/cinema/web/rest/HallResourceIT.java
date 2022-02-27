package com.cinema.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cinema.IntegrationTest;
import com.cinema.domain.Hall;
import com.cinema.repository.HallRepository;
import com.cinema.service.dto.HallDTO;
import com.cinema.service.mapper.HallMapper;
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
 * Integration tests for the {@link HallResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HallResourceIT {

    private static final Integer DEFAULT_COLS_HALL = 1;
    private static final Integer UPDATED_COLS_HALL = 2;

    private static final Integer DEFAULT_ROWS_HALL = 1;
    private static final Integer UPDATED_ROWS_HALL = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/halls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private HallMapper hallMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHallMockMvc;

    private Hall hall;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hall createEntity(EntityManager em) {
        Hall hall = new Hall().cols_hall(DEFAULT_COLS_HALL).rows_hall(DEFAULT_ROWS_HALL).name(DEFAULT_NAME);
        return hall;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hall createUpdatedEntity(EntityManager em) {
        Hall hall = new Hall().cols_hall(UPDATED_COLS_HALL).rows_hall(UPDATED_ROWS_HALL).name(UPDATED_NAME);
        return hall;
    }

    @BeforeEach
    public void initTest() {
        hall = createEntity(em);
    }

    @Test
    @Transactional
    void createHall() throws Exception {
        int databaseSizeBeforeCreate = hallRepository.findAll().size();
        // Create the Hall
        HallDTO hallDTO = hallMapper.toDto(hall);
        restHallMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hallDTO)))
            .andExpect(status().isCreated());

        // Validate the Hall in the database
        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeCreate + 1);
        Hall testHall = hallList.get(hallList.size() - 1);
        assertThat(testHall.getCols_hall()).isEqualTo(DEFAULT_COLS_HALL);
        assertThat(testHall.getRows_hall()).isEqualTo(DEFAULT_ROWS_HALL);
        assertThat(testHall.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createHallWithExistingId() throws Exception {
        // Create the Hall with an existing ID
        hall.setId(1L);
        HallDTO hallDTO = hallMapper.toDto(hall);

        int databaseSizeBeforeCreate = hallRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHallMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hallDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Hall in the database
        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCols_hallIsRequired() throws Exception {
        int databaseSizeBeforeTest = hallRepository.findAll().size();
        // set the field null
        hall.setCols_hall(null);

        // Create the Hall, which fails.
        HallDTO hallDTO = hallMapper.toDto(hall);

        restHallMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hallDTO)))
            .andExpect(status().isBadRequest());

        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRows_hallIsRequired() throws Exception {
        int databaseSizeBeforeTest = hallRepository.findAll().size();
        // set the field null
        hall.setRows_hall(null);

        // Create the Hall, which fails.
        HallDTO hallDTO = hallMapper.toDto(hall);

        restHallMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hallDTO)))
            .andExpect(status().isBadRequest());

        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = hallRepository.findAll().size();
        // set the field null
        hall.setName(null);

        // Create the Hall, which fails.
        HallDTO hallDTO = hallMapper.toDto(hall);

        restHallMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hallDTO)))
            .andExpect(status().isBadRequest());

        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHalls() throws Exception {
        // Initialize the database
        hallRepository.saveAndFlush(hall);

        // Get all the hallList
        restHallMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hall.getId().intValue())))
            .andExpect(jsonPath("$.[*].cols_hall").value(hasItem(DEFAULT_COLS_HALL)))
            .andExpect(jsonPath("$.[*].rows_hall").value(hasItem(DEFAULT_ROWS_HALL)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getHall() throws Exception {
        // Initialize the database
        hallRepository.saveAndFlush(hall);

        // Get the hall
        restHallMockMvc
            .perform(get(ENTITY_API_URL_ID, hall.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hall.getId().intValue()))
            .andExpect(jsonPath("$.cols_hall").value(DEFAULT_COLS_HALL))
            .andExpect(jsonPath("$.rows_hall").value(DEFAULT_ROWS_HALL))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingHall() throws Exception {
        // Get the hall
        restHallMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHall() throws Exception {
        // Initialize the database
        hallRepository.saveAndFlush(hall);

        int databaseSizeBeforeUpdate = hallRepository.findAll().size();

        // Update the hall
        Hall updatedHall = hallRepository.findById(hall.getId()).get();
        // Disconnect from session so that the updates on updatedHall are not directly saved in db
        em.detach(updatedHall);
        updatedHall.cols_hall(UPDATED_COLS_HALL).rows_hall(UPDATED_ROWS_HALL).name(UPDATED_NAME);
        HallDTO hallDTO = hallMapper.toDto(updatedHall);

        restHallMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hallDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hallDTO))
            )
            .andExpect(status().isOk());

        // Validate the Hall in the database
        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeUpdate);
        Hall testHall = hallList.get(hallList.size() - 1);
        assertThat(testHall.getCols_hall()).isEqualTo(UPDATED_COLS_HALL);
        assertThat(testHall.getRows_hall()).isEqualTo(UPDATED_ROWS_HALL);
        assertThat(testHall.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingHall() throws Exception {
        int databaseSizeBeforeUpdate = hallRepository.findAll().size();
        hall.setId(count.incrementAndGet());

        // Create the Hall
        HallDTO hallDTO = hallMapper.toDto(hall);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHallMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hallDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hallDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hall in the database
        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHall() throws Exception {
        int databaseSizeBeforeUpdate = hallRepository.findAll().size();
        hall.setId(count.incrementAndGet());

        // Create the Hall
        HallDTO hallDTO = hallMapper.toDto(hall);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHallMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hallDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hall in the database
        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHall() throws Exception {
        int databaseSizeBeforeUpdate = hallRepository.findAll().size();
        hall.setId(count.incrementAndGet());

        // Create the Hall
        HallDTO hallDTO = hallMapper.toDto(hall);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHallMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hallDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hall in the database
        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHallWithPatch() throws Exception {
        // Initialize the database
        hallRepository.saveAndFlush(hall);

        int databaseSizeBeforeUpdate = hallRepository.findAll().size();

        // Update the hall using partial update
        Hall partialUpdatedHall = new Hall();
        partialUpdatedHall.setId(hall.getId());

        partialUpdatedHall.name(UPDATED_NAME);

        restHallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHall.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHall))
            )
            .andExpect(status().isOk());

        // Validate the Hall in the database
        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeUpdate);
        Hall testHall = hallList.get(hallList.size() - 1);
        assertThat(testHall.getCols_hall()).isEqualTo(DEFAULT_COLS_HALL);
        assertThat(testHall.getRows_hall()).isEqualTo(DEFAULT_ROWS_HALL);
        assertThat(testHall.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateHallWithPatch() throws Exception {
        // Initialize the database
        hallRepository.saveAndFlush(hall);

        int databaseSizeBeforeUpdate = hallRepository.findAll().size();

        // Update the hall using partial update
        Hall partialUpdatedHall = new Hall();
        partialUpdatedHall.setId(hall.getId());

        partialUpdatedHall.cols_hall(UPDATED_COLS_HALL).rows_hall(UPDATED_ROWS_HALL).name(UPDATED_NAME);

        restHallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHall.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHall))
            )
            .andExpect(status().isOk());

        // Validate the Hall in the database
        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeUpdate);
        Hall testHall = hallList.get(hallList.size() - 1);
        assertThat(testHall.getCols_hall()).isEqualTo(UPDATED_COLS_HALL);
        assertThat(testHall.getRows_hall()).isEqualTo(UPDATED_ROWS_HALL);
        assertThat(testHall.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingHall() throws Exception {
        int databaseSizeBeforeUpdate = hallRepository.findAll().size();
        hall.setId(count.incrementAndGet());

        // Create the Hall
        HallDTO hallDTO = hallMapper.toDto(hall);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hallDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hallDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hall in the database
        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHall() throws Exception {
        int databaseSizeBeforeUpdate = hallRepository.findAll().size();
        hall.setId(count.incrementAndGet());

        // Create the Hall
        HallDTO hallDTO = hallMapper.toDto(hall);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hallDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hall in the database
        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHall() throws Exception {
        int databaseSizeBeforeUpdate = hallRepository.findAll().size();
        hall.setId(count.incrementAndGet());

        // Create the Hall
        HallDTO hallDTO = hallMapper.toDto(hall);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHallMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(hallDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hall in the database
        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHall() throws Exception {
        // Initialize the database
        hallRepository.saveAndFlush(hall);

        int databaseSizeBeforeDelete = hallRepository.findAll().size();

        // Delete the hall
        restHallMockMvc
            .perform(delete(ENTITY_API_URL_ID, hall.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
