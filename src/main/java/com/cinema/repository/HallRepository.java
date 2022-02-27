package com.cinema.repository;

import com.cinema.domain.Hall;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Hall entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {}
