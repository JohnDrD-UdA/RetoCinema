package com.cinema.service;

import com.cinema.domain.Movie;
import com.cinema.domain.Moviefunction;
import com.cinema.service.dto.MoviefunctionDTO;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.cinema.domain.Moviefunction}.
 */
public interface MoviefunctionService {
    /**
     * Save a moviefunction.
     *
     * @param moviefunctionDTO the entity to save.
     * @return the persisted entity.
     */
    MoviefunctionDTO save(MoviefunctionDTO moviefunctionDTO);

    /**
     * Partially updates a moviefunction.
     *
     * @param moviefunctionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MoviefunctionDTO> partialUpdate(MoviefunctionDTO moviefunctionDTO);

    /**
     * Get all the moviefunctions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MoviefunctionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" moviefunction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MoviefunctionDTO> findOne(Long id);

    /**
     * Delete the "id" moviefunction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    public boolean createMovieFunction(Moviefunction moviefunction);
    /**
     * Get all functions related to a given movie function
     * @param movie
     * @return List of MovieFunctions
     */
    List<Moviefunction> getAllByMovie(Movie movie);
    
}
