package com.cinema.service.impl;

import com.cinema.domain.Movie;
import com.cinema.domain.Moviefunction;
import com.cinema.repository.MovieRepository;
import com.cinema.service.MovieService;
import com.cinema.service.MoviefunctionService;
import com.cinema.service.dto.MovieDTO;
import com.cinema.service.mapper.MovieMapper;

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
 * Service Implementation for managing {@link Movie}.
 */
@Service
@Transactional
public class MovieServiceImpl implements MovieService {

    private final Logger log = LoggerFactory.getLogger(MovieServiceImpl.class);

    private final MovieRepository movieRepository;

    private final MovieMapper movieMapper;

    @Autowired
    private MoviefunctionService moviefunctionService;

    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    @Override
    public MovieDTO save(MovieDTO movieDTO) {
        log.debug("Request to save Movie : {}", movieDTO);
        Movie movie = movieMapper.toEntity(movieDTO);
        movie = movieRepository.save(movie);
        return movieMapper.toDto(movie);
    }

    @Override
    public Optional<MovieDTO> partialUpdate(MovieDTO movieDTO) {
        log.debug("Request to partially update Movie : {}", movieDTO);

        return movieRepository
            .findById(movieDTO.getId())
            .map(existingMovie -> {
                movieMapper.partialUpdate(existingMovie, movieDTO);

                return existingMovie;
            })
            .map(movieRepository::save)
            .map(movieMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MovieDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Movies");
        return movieRepository.findAll(pageable).map(movieMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MovieDTO> findOne(Long id) {
        log.debug("Request to get Movie : {}", id);
        return movieRepository.findById(id).map(movieMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Movie : {}", id);
        Movie movie=new Movie();
        movie.setId(id);
        List<Moviefunction> moviefunctions= moviefunctionService.getAllByMovie(movie);
        for (Moviefunction moviefunction : moviefunctions) {
            moviefunctionService.delete(moviefunction.getId());
        }
        movieRepository.deleteById(id);
    }

    @Override
    public List<Movie> getAll() {
        return movieRepository.findAll();
    }
}
