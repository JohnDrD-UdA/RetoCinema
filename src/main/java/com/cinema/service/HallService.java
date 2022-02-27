package com.cinema.service;

import com.cinema.service.dto.HallDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.cinema.domain.Hall}.
 */
public interface HallService {
    /**
     * Save a hall.
     *
     * @param hallDTO the entity to save.
     * @return the persisted entity.
     */
    HallDTO save(HallDTO hallDTO);

    /**
     * Partially updates a hall.
     *
     * @param hallDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HallDTO> partialUpdate(HallDTO hallDTO);

    /**
     * Get all the halls.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HallDTO> findAll(Pageable pageable);

    /**
     * Get the "id" hall.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HallDTO> findOne(Long id);

    /**
     * Delete the "id" hall.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
