package com.cinema.repository;

import java.util.List;

import com.cinema.domain.Hall;
import com.cinema.domain.Movie;
import com.cinema.domain.Moviefunction;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



/**
 * Spring Data SQL repository for the Moviefunction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MoviefunctionRepository extends JpaRepository<Moviefunction, Long> {
    @Query("select f from Moviefunction f where f.movie = :movie and f.active_movie_function=true")
    List<Moviefunction> findByMovie(@Param("movie") Movie movie);

    @Query("select f from Moviefunction f where f.hall = :hally")
    List<Moviefunction> findByHaList(@Param("hally") Hall hall);
}
