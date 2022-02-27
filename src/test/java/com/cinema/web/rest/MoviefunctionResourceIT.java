package com.cinema.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cinema.IntegrationTest;
import com.cinema.domain.Hall;
import com.cinema.domain.Movie;
import com.cinema.domain.Moviefunction;
import com.cinema.repository.MoviefunctionRepository;
import com.cinema.service.dto.MoviefunctionDTO;
import com.cinema.service.mapper.MoviefunctionMapper;
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
 * Integration tests for the {@link MoviefunctionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MoviefunctionResourceIT {

    private static final String DEFAULT_MOVIE_DATE_TIME = "AAAAAAAAAA";
    private static final String UPDATED_MOVIE_DATE_TIME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE_MOVIE_FUNCTION = false;
    private static final Boolean UPDATED_ACTIVE_MOVIE_FUNCTION = true;

    private static final String ENTITY_API_URL = "/api/moviefunctions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MoviefunctionRepository moviefunctionRepository;

    @Autowired
    private MoviefunctionMapper moviefunctionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMoviefunctionMockMvc;

    private Moviefunction moviefunction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Moviefunction createEntity(EntityManager em) {
        Moviefunction moviefunction = new Moviefunction()
            .movie_date_time(DEFAULT_MOVIE_DATE_TIME)
            .active_movie_function(DEFAULT_ACTIVE_MOVIE_FUNCTION);
        // Add required entity
        Movie movie;
        if (TestUtil.findAll(em, Movie.class).isEmpty()) {
            movie = MovieResourceIT.createEntity(em);
            em.persist(movie);
            em.flush();
        } else {
            movie = TestUtil.findAll(em, Movie.class).get(0);
        }
        moviefunction.setMovie(movie);
        // Add required entity
        Hall hall;
        if (TestUtil.findAll(em, Hall.class).isEmpty()) {
            hall = HallResourceIT.createEntity(em);
            em.persist(hall);
            em.flush();
        } else {
            hall = TestUtil.findAll(em, Hall.class).get(0);
        }
        moviefunction.setHall(hall);
        return moviefunction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Moviefunction createUpdatedEntity(EntityManager em) {
        Moviefunction moviefunction = new Moviefunction()
            .movie_date_time(UPDATED_MOVIE_DATE_TIME)
            .active_movie_function(UPDATED_ACTIVE_MOVIE_FUNCTION);
        // Add required entity
        Movie movie;
        if (TestUtil.findAll(em, Movie.class).isEmpty()) {
            movie = MovieResourceIT.createUpdatedEntity(em);
            em.persist(movie);
            em.flush();
        } else {
            movie = TestUtil.findAll(em, Movie.class).get(0);
        }
        moviefunction.setMovie(movie);
        // Add required entity
        Hall hall;
        if (TestUtil.findAll(em, Hall.class).isEmpty()) {
            hall = HallResourceIT.createUpdatedEntity(em);
            em.persist(hall);
            em.flush();
        } else {
            hall = TestUtil.findAll(em, Hall.class).get(0);
        }
        moviefunction.setHall(hall);
        return moviefunction;
    }

    @BeforeEach
    public void initTest() {
        moviefunction = createEntity(em);
    }

    @Test
    @Transactional
    void createMoviefunction() throws Exception {
        int databaseSizeBeforeCreate = moviefunctionRepository.findAll().size();
        // Create the Moviefunction
        MoviefunctionDTO moviefunctionDTO = moviefunctionMapper.toDto(moviefunction);
        restMoviefunctionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moviefunctionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Moviefunction in the database
        List<Moviefunction> moviefunctionList = moviefunctionRepository.findAll();
        assertThat(moviefunctionList).hasSize(databaseSizeBeforeCreate + 1);
        Moviefunction testMoviefunction = moviefunctionList.get(moviefunctionList.size() - 1);
        assertThat(testMoviefunction.getMovie_date_time()).isEqualTo(DEFAULT_MOVIE_DATE_TIME);
        assertThat(testMoviefunction.getActive_movie_function()).isEqualTo(DEFAULT_ACTIVE_MOVIE_FUNCTION);
    }

    @Test
    @Transactional
    void createMoviefunctionWithExistingId() throws Exception {
        // Create the Moviefunction with an existing ID
        moviefunction.setId(1L);
        MoviefunctionDTO moviefunctionDTO = moviefunctionMapper.toDto(moviefunction);

        int databaseSizeBeforeCreate = moviefunctionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoviefunctionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moviefunctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Moviefunction in the database
        List<Moviefunction> moviefunctionList = moviefunctionRepository.findAll();
        assertThat(moviefunctionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMovie_date_timeIsRequired() throws Exception {
        int databaseSizeBeforeTest = moviefunctionRepository.findAll().size();
        // set the field null
        moviefunction.setMovie_date_time(null);

        // Create the Moviefunction, which fails.
        MoviefunctionDTO moviefunctionDTO = moviefunctionMapper.toDto(moviefunction);

        restMoviefunctionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moviefunctionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Moviefunction> moviefunctionList = moviefunctionRepository.findAll();
        assertThat(moviefunctionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActive_movie_functionIsRequired() throws Exception {
        int databaseSizeBeforeTest = moviefunctionRepository.findAll().size();
        // set the field null
        moviefunction.setActive_movie_function(null);

        // Create the Moviefunction, which fails.
        MoviefunctionDTO moviefunctionDTO = moviefunctionMapper.toDto(moviefunction);

        restMoviefunctionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moviefunctionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Moviefunction> moviefunctionList = moviefunctionRepository.findAll();
        assertThat(moviefunctionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMoviefunctions() throws Exception {
        // Initialize the database
        moviefunctionRepository.saveAndFlush(moviefunction);

        // Get all the moviefunctionList
        restMoviefunctionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moviefunction.getId().intValue())))
            .andExpect(jsonPath("$.[*].movie_date_time").value(hasItem(DEFAULT_MOVIE_DATE_TIME)))
            .andExpect(jsonPath("$.[*].active_movie_function").value(hasItem(DEFAULT_ACTIVE_MOVIE_FUNCTION.booleanValue())));
    }

    @Test
    @Transactional
    void getMoviefunction() throws Exception {
        // Initialize the database
        moviefunctionRepository.saveAndFlush(moviefunction);

        // Get the moviefunction
        restMoviefunctionMockMvc
            .perform(get(ENTITY_API_URL_ID, moviefunction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moviefunction.getId().intValue()))
            .andExpect(jsonPath("$.movie_date_time").value(DEFAULT_MOVIE_DATE_TIME))
            .andExpect(jsonPath("$.active_movie_function").value(DEFAULT_ACTIVE_MOVIE_FUNCTION.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingMoviefunction() throws Exception {
        // Get the moviefunction
        restMoviefunctionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMoviefunction() throws Exception {
        // Initialize the database
        moviefunctionRepository.saveAndFlush(moviefunction);

        int databaseSizeBeforeUpdate = moviefunctionRepository.findAll().size();

        // Update the moviefunction
        Moviefunction updatedMoviefunction = moviefunctionRepository.findById(moviefunction.getId()).get();
        // Disconnect from session so that the updates on updatedMoviefunction are not directly saved in db
        em.detach(updatedMoviefunction);
        updatedMoviefunction.movie_date_time(UPDATED_MOVIE_DATE_TIME).active_movie_function(UPDATED_ACTIVE_MOVIE_FUNCTION);
        MoviefunctionDTO moviefunctionDTO = moviefunctionMapper.toDto(updatedMoviefunction);

        restMoviefunctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moviefunctionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moviefunctionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Moviefunction in the database
        List<Moviefunction> moviefunctionList = moviefunctionRepository.findAll();
        assertThat(moviefunctionList).hasSize(databaseSizeBeforeUpdate);
        Moviefunction testMoviefunction = moviefunctionList.get(moviefunctionList.size() - 1);
        assertThat(testMoviefunction.getMovie_date_time()).isEqualTo(UPDATED_MOVIE_DATE_TIME);
        assertThat(testMoviefunction.getActive_movie_function()).isEqualTo(UPDATED_ACTIVE_MOVIE_FUNCTION);
    }

    @Test
    @Transactional
    void putNonExistingMoviefunction() throws Exception {
        int databaseSizeBeforeUpdate = moviefunctionRepository.findAll().size();
        moviefunction.setId(count.incrementAndGet());

        // Create the Moviefunction
        MoviefunctionDTO moviefunctionDTO = moviefunctionMapper.toDto(moviefunction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoviefunctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moviefunctionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moviefunctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Moviefunction in the database
        List<Moviefunction> moviefunctionList = moviefunctionRepository.findAll();
        assertThat(moviefunctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMoviefunction() throws Exception {
        int databaseSizeBeforeUpdate = moviefunctionRepository.findAll().size();
        moviefunction.setId(count.incrementAndGet());

        // Create the Moviefunction
        MoviefunctionDTO moviefunctionDTO = moviefunctionMapper.toDto(moviefunction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoviefunctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moviefunctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Moviefunction in the database
        List<Moviefunction> moviefunctionList = moviefunctionRepository.findAll();
        assertThat(moviefunctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMoviefunction() throws Exception {
        int databaseSizeBeforeUpdate = moviefunctionRepository.findAll().size();
        moviefunction.setId(count.incrementAndGet());

        // Create the Moviefunction
        MoviefunctionDTO moviefunctionDTO = moviefunctionMapper.toDto(moviefunction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoviefunctionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moviefunctionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Moviefunction in the database
        List<Moviefunction> moviefunctionList = moviefunctionRepository.findAll();
        assertThat(moviefunctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMoviefunctionWithPatch() throws Exception {
        // Initialize the database
        moviefunctionRepository.saveAndFlush(moviefunction);

        int databaseSizeBeforeUpdate = moviefunctionRepository.findAll().size();

        // Update the moviefunction using partial update
        Moviefunction partialUpdatedMoviefunction = new Moviefunction();
        partialUpdatedMoviefunction.setId(moviefunction.getId());

        partialUpdatedMoviefunction.movie_date_time(UPDATED_MOVIE_DATE_TIME);

        restMoviefunctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoviefunction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMoviefunction))
            )
            .andExpect(status().isOk());

        // Validate the Moviefunction in the database
        List<Moviefunction> moviefunctionList = moviefunctionRepository.findAll();
        assertThat(moviefunctionList).hasSize(databaseSizeBeforeUpdate);
        Moviefunction testMoviefunction = moviefunctionList.get(moviefunctionList.size() - 1);
        assertThat(testMoviefunction.getMovie_date_time()).isEqualTo(UPDATED_MOVIE_DATE_TIME);
        assertThat(testMoviefunction.getActive_movie_function()).isEqualTo(DEFAULT_ACTIVE_MOVIE_FUNCTION);
    }

    @Test
    @Transactional
    void fullUpdateMoviefunctionWithPatch() throws Exception {
        // Initialize the database
        moviefunctionRepository.saveAndFlush(moviefunction);

        int databaseSizeBeforeUpdate = moviefunctionRepository.findAll().size();

        // Update the moviefunction using partial update
        Moviefunction partialUpdatedMoviefunction = new Moviefunction();
        partialUpdatedMoviefunction.setId(moviefunction.getId());

        partialUpdatedMoviefunction.movie_date_time(UPDATED_MOVIE_DATE_TIME).active_movie_function(UPDATED_ACTIVE_MOVIE_FUNCTION);

        restMoviefunctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoviefunction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMoviefunction))
            )
            .andExpect(status().isOk());

        // Validate the Moviefunction in the database
        List<Moviefunction> moviefunctionList = moviefunctionRepository.findAll();
        assertThat(moviefunctionList).hasSize(databaseSizeBeforeUpdate);
        Moviefunction testMoviefunction = moviefunctionList.get(moviefunctionList.size() - 1);
        assertThat(testMoviefunction.getMovie_date_time()).isEqualTo(UPDATED_MOVIE_DATE_TIME);
        assertThat(testMoviefunction.getActive_movie_function()).isEqualTo(UPDATED_ACTIVE_MOVIE_FUNCTION);
    }

    @Test
    @Transactional
    void patchNonExistingMoviefunction() throws Exception {
        int databaseSizeBeforeUpdate = moviefunctionRepository.findAll().size();
        moviefunction.setId(count.incrementAndGet());

        // Create the Moviefunction
        MoviefunctionDTO moviefunctionDTO = moviefunctionMapper.toDto(moviefunction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoviefunctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moviefunctionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(moviefunctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Moviefunction in the database
        List<Moviefunction> moviefunctionList = moviefunctionRepository.findAll();
        assertThat(moviefunctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMoviefunction() throws Exception {
        int databaseSizeBeforeUpdate = moviefunctionRepository.findAll().size();
        moviefunction.setId(count.incrementAndGet());

        // Create the Moviefunction
        MoviefunctionDTO moviefunctionDTO = moviefunctionMapper.toDto(moviefunction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoviefunctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(moviefunctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Moviefunction in the database
        List<Moviefunction> moviefunctionList = moviefunctionRepository.findAll();
        assertThat(moviefunctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMoviefunction() throws Exception {
        int databaseSizeBeforeUpdate = moviefunctionRepository.findAll().size();
        moviefunction.setId(count.incrementAndGet());

        // Create the Moviefunction
        MoviefunctionDTO moviefunctionDTO = moviefunctionMapper.toDto(moviefunction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoviefunctionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(moviefunctionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Moviefunction in the database
        List<Moviefunction> moviefunctionList = moviefunctionRepository.findAll();
        assertThat(moviefunctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMoviefunction() throws Exception {
        // Initialize the database
        moviefunctionRepository.saveAndFlush(moviefunction);

        int databaseSizeBeforeDelete = moviefunctionRepository.findAll().size();

        // Delete the moviefunction
        restMoviefunctionMockMvc
            .perform(delete(ENTITY_API_URL_ID, moviefunction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Moviefunction> moviefunctionList = moviefunctionRepository.findAll();
        assertThat(moviefunctionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
