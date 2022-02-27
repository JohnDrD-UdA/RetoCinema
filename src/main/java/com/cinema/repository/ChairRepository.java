package com.cinema.repository;

import java.util.List;

import com.cinema.domain.Booking;
import com.cinema.domain.Chair;
import com.cinema.domain.Moviefunction;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Chair entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChairRepository extends JpaRepository<Chair, Long> {
    @Query("select c from Chair c where c.moviefunction= :MF")
    List<Chair> getChairsFromMF(@Param("MF") Moviefunction movieF);

    @Query("select c from Chair c where c.booking= :book")
    List<Chair> getChairsFromBooking(@Param("book") Booking booking);

    @Query("select c from Chair c where c.booking!=null and c.moviefunction= :fm")
    List<Chair> getBookedChairsFromFun(@Param("fm") Moviefunction moviefunction);
}
