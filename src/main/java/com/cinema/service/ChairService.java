package com.cinema.service;

import com.cinema.domain.Booking;
import com.cinema.domain.Chair;
import com.cinema.domain.Moviefunction;
import com.cinema.service.dto.ChairDTO;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.cinema.domain.Chair}.
 */
public interface ChairService {
    /**
     * Save a chair.
     *
     * @param chairDTO the entity to save.
     * @return the persisted entity.
     */
    ChairDTO save(ChairDTO chairDTO);

    /**
     * Partially updates a chair.
     *
     * @param chairDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ChairDTO> partialUpdate(ChairDTO chairDTO);

    /**
     * Get all the chairs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChairDTO> findAll(Pageable pageable);

    /**
     * Get the "id" chair.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChairDTO> findOne(Long id);

    /**
     * Delete the "id" chair.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

/**
 * Set the status of a list of chairs as true
 * @param chairs
 * @return boolean status
 */
    public boolean freeChair(List<Chair> chairs);

    /**
     * Set the booking filed on a list of Chairs as the given Booking
     * @param chairs
     * @param booking
     * @return boolean status
     */
    public boolean bookChairIntoLog(List<Chair> chairs, Booking booking);

    /**
     * Set the status of a list of chairs as false
     * @param chairs
     * @return boolean status
     */
    public List<Chair> bookChair(List<Chair> chairs);
     /**
      * Return a List with all the chairs related to a movie Function.
      * @param moviefunction
      * @return List of Chairs
      */
    public List<List<Chair>> getByMovieFunction(Moviefunction moviefunction);

    /**Return a List with all the chairs related to a booking.
     * 
     * @param booking
     * @return List of Chairs
     */
    public List<ChairDTO> getByBooking(Booking booking);
}
