package com.cinema.service.impl;

import com.cinema.domain.Booking;
import com.cinema.domain.Chair;
import com.cinema.repository.BookingRepository;
import com.cinema.service.BookingService;
import com.cinema.service.ChairService;
import com.cinema.service.dto.BookingDTO;
import com.cinema.service.dto.ChairDTO;
import com.cinema.service.mapper.BookingMapper;
import com.cinema.service.mapper.UtilMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Booking}.
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;
    @Autowired
    private ChairService chairService;

    public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public BookingDTO save(BookingDTO bookingDTO) {
        log.debug("Request to save Booking : {}", bookingDTO);
        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking = bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }
    @Override
    public Booking saveBooking(Booking booking){
        log.debug("request To save Booking: {}", booking);
        booking=bookingRepository.save(booking);
        return booking;
    }

    @Override
    public Optional<BookingDTO> partialUpdate(BookingDTO bookingDTO) {
        log.debug("Request to partially update Booking : {}", bookingDTO);

        return bookingRepository
            .findById(bookingDTO.getId())
            .map(existingBooking -> {
                bookingMapper.partialUpdate(existingBooking, bookingDTO);

                return existingBooking;
            })
            .map(bookingRepository::save)
            .map(bookingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bookings");
        return bookingRepository.findAll(pageable).map(bookingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookingDTO> findOne(Long id) {
        log.debug("Request to get Booking : {}", id);
        return bookingRepository.findById(id).map(bookingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Booking : {}", id);
        Booking booking=new Booking();
        booking.setId(id);
        try {
            bookingRepository.getById(id);
        } catch (Exception e) {
            System.out.println(e.toString());
            return;
        }
        UtilMapper utilMapper=new UtilMapper();
        List<Chair> chairs=utilMapper.convertDTOtoModel(chairService.getByBooking(booking));
        if(chairService.freeChair(chairs)){
            bookingRepository.deleteById(id); 
        }
        return;
    }

    @Override
    public List<List<ChairDTO>> getCurrentUserBookings() {
        return queryChairsByCUser();
    }

    private List<List<ChairDTO>> queryChairsByCUser(){
        List<Booking> bookings=this.bookingRepository.findByUserIsCurrentUser();
        List<List<ChairDTO>> chairs=new ArrayList<>();
        List<ChairDTO> auxChairs= new ArrayList<>();
        for (Booking booking : bookings) {
            auxChairs=chairService.getByBooking(booking);
            if(auxChairs.isEmpty()){bookingRepository.delete(booking);}
            else{chairs.add(auxChairs);}   
        }
        return chairs;
    }
}
