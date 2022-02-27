package com.cinema.service;

import com.cinema.domain.Movie;
import com.cinema.service.dto.MovieDTO;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.cinema.domain.Movie}.
 */
public interface MovieService {
    /**
     * Save a movie.
     *
     * @param movieDTO the entity to save.
     * @return the persisted entity.
     */
    MovieDTO save(MovieDTO movieDTO);

    /**
     * Partially updates a movie.
     *
     * @param movieDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MovieDTO> partialUpdate(MovieDTO movieDTO);

    /**
     * Get all the movies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MovieDTO> findAll(Pageable pageable);

    /**
     * Get the "id" movie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MovieDTO> findOne(Long id);

    /**
     * Delete the "id" movie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    /**
     * return all movies as a list
     * @return
     */
    List<Movie> getAll();
}
