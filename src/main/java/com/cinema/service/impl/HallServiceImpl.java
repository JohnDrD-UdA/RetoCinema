package com.cinema.service.impl;

import com.cinema.domain.Hall;
import com.cinema.domain.Moviefunction;
import com.cinema.repository.HallRepository;
import com.cinema.repository.MoviefunctionRepository;
import com.cinema.service.HallService;
import com.cinema.service.MoviefunctionService;
import com.cinema.service.dto.HallDTO;
import com.cinema.service.mapper.HallMapper;

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
 * Service Implementation for managing {@link Hall}.
 */
@Service
@Transactional
public class HallServiceImpl implements HallService {

    private final Logger log = LoggerFactory.getLogger(HallServiceImpl.class);

    private final HallRepository hallRepository;

    private final HallMapper hallMapper;

    private final MoviefunctionRepository moviefunctionRepository;
    @Autowired
    private MoviefunctionService moviefunctionService;

    public HallServiceImpl(HallRepository hallRepository, HallMapper hallMapper, MoviefunctionRepository moviefunctionRepository) {
        this.hallRepository = hallRepository;
       this.hallMapper = hallMapper;
       this.moviefunctionRepository=moviefunctionRepository;
    }

    @Override
    public HallDTO save(HallDTO hallDTO) {
        log.debug("Request to save Hall : {}", hallDTO);
        Hall hall = hallMapper.toEntity(hallDTO);
        hall = hallRepository.save(hall);
        return hallMapper.toDto(hall);
    }

    @Override
    public Optional<HallDTO> partialUpdate(HallDTO hallDTO) {
        log.debug("Request to partially update Hall : {}", hallDTO);

        return hallRepository
            .findById(hallDTO.getId())
            .map(existingHall -> {
                hallMapper.partialUpdate(existingHall, hallDTO);

                return existingHall;
            })
            .map(hallRepository::save)
            .map(hallMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HallDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Halls");
        return hallRepository.findAll(pageable).map(hallMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HallDTO> findOne(Long id) {
        log.debug("Request to get Hall : {}", id);
        return hallRepository.findById(id).map(hallMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Hall : {}", id);
        Hall hall= new Hall();
        hall.setId(id);
        List<Moviefunction> moviefunctions=moviefunctionRepository.findByHaList(hall);
        for (Moviefunction moviefunction : moviefunctions) {
            moviefunctionService.delete(moviefunction.getId());
        }
        hallRepository.deleteById(id);
    }
}
