package com.cinema.service.impl;

import com.cinema.domain.Chair;
import com.cinema.domain.Hall;
import com.cinema.domain.Movie;
import com.cinema.domain.Moviefunction;
import com.cinema.repository.ChairRepository;
import com.cinema.repository.HallRepository;
import com.cinema.repository.MovieRepository;
import com.cinema.repository.MoviefunctionRepository;
import com.cinema.service.BookingService;
import com.cinema.service.MoviefunctionService;
import com.cinema.service.dto.MoviefunctionDTO;
import com.cinema.service.mapper.MoviefunctionMapper;

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
 * Service Implementation for managing {@link Moviefunction}.
 */
@Service
public class MoviefunctionServiceImpl implements MoviefunctionService {

    private final Logger log = LoggerFactory.getLogger(MoviefunctionServiceImpl.class);

    private final MoviefunctionRepository moviefunctionRepository;

    private final HallRepository hallRepository;

    private final MovieRepository movieRepository;

    private final MoviefunctionMapper moviefunctionMapper;

    private final ChairRepository chairRepository;

    @Autowired
    private BookingService bookingService; 

    public MoviefunctionServiceImpl(MoviefunctionRepository moviefunctionRepository, MoviefunctionMapper moviefunctionMapper, MovieRepository movieRepository, HallRepository hallRepository,ChairRepository chairRepository) {
        this.moviefunctionRepository = moviefunctionRepository;
        this.moviefunctionMapper = moviefunctionMapper;
        this.hallRepository=hallRepository;
        this.movieRepository=movieRepository;
        this.chairRepository=chairRepository;
    }

    @Override
    public MoviefunctionDTO save(MoviefunctionDTO moviefunctionDTO) {
        log.debug("Request to save Moviefunction : {}", moviefunctionDTO);
        Moviefunction moviefunction = moviefunctionMapper.toEntity(moviefunctionDTO);

        moviefunction = moviefunctionRepository.save(moviefunction);
        try {
            if(chairRepository.getChairsFromMF(moviefunction).isEmpty()){createChairsForFunction(moviefunction);}   
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return moviefunctionMapper.toDto(moviefunction);
    }

    @Override
    public Optional<MoviefunctionDTO> partialUpdate(MoviefunctionDTO moviefunctionDTO) {
        log.debug("Request to partially update Moviefunction : {}", moviefunctionDTO);

        return moviefunctionRepository
            .findById(moviefunctionDTO.getId())
            .map(existingMoviefunction -> {
                moviefunctionMapper.partialUpdate(existingMoviefunction, moviefunctionDTO);

                return existingMoviefunction;
            })
            .map(moviefunctionRepository::save)
            .map(moviefunctionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MoviefunctionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Moviefunctions");
        return moviefunctionRepository.findAll(pageable).map(moviefunctionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MoviefunctionDTO> findOne(Long id) {
        log.debug("Request to get Moviefunction : {}", id);
        return moviefunctionRepository.findById(id).map(moviefunctionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Moviefunction : {}", id);
        Moviefunction movieF=new Moviefunction();
        movieF.setId(id);
        List<Chair> bookedChairs=chairRepository.getBookedChairsFromFun(movieF);
        for(int i=0;i<bookedChairs.size();i++){
            bookingService.delete(bookedChairs.get(i).getBooking().getId());
        }
        List<Chair> chairs=chairRepository.getChairsFromMF(movieF);
        chairRepository.deleteAll(chairs);
        moviefunctionRepository.deleteById(id);
    }

    @Override
    public boolean createMovieFunction(Moviefunction moviefunction) {
        try {
            log.debug("Request to create Moviefunction and his chairs");
            Hall hallInfo= hallRepository.getById(moviefunction.getHall().getId());
            Movie movie=movieRepository.getById(moviefunction.getMovie().getId());
            moviefunction.setHall(hallInfo);
            moviefunction.setMovie(movie);
            Moviefunction savedMoviefunction=moviefunctionRepository.save(moviefunction);
            createChairsForFunction(savedMoviefunction);
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }
    private void createChairsForFunction(Moviefunction moviefunction){
        String rowID="ABCDEFGHIJKLMNÑOPQESTUVXYZabcdefghij";
        List<Chair> chairs=new ArrayList<>();

        for(int i=0;i<moviefunction.getHall().getRows_hall();i++){
            String rowIndex=rowID.substring(i,i+1);

            for(int j=0;j<moviefunction.getHall().getCols_hall();j++){
                
                Chair chair=new Chair();

                String chairLocation=rowIndex+"-"+String.valueOf(j+1);
                chair.setLocation(chairLocation);
                chair.setAvaible_chair(true);
                chair.setMoviefunction(moviefunction);
                chairs.add(chair);
                System.out.println("Chair in "+ chairLocation+" added"+chair.getId());
            }

        }
        System.out.println(chairs);
        try{
            for (Chair chair : chairs) {
                chairRepository.save(chair);
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        
    }

    @Override
    public List<Moviefunction> getAllByMovie(Movie movie) {
        return moviefunctionRepository.findByMovie(movie);
    }
}

