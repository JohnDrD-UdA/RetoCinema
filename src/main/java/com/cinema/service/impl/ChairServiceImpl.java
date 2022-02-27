package com.cinema.service.impl;

import com.cinema.domain.Booking;
import com.cinema.domain.Chair;
import com.cinema.domain.Moviefunction;
import com.cinema.repository.ChairRepository;
import com.cinema.service.ChairService;
import com.cinema.service.dto.ChairDTO;
import com.cinema.service.mapper.ChairMapper;
import com.cinema.service.mapper.UtilMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.undertow.servlet.compat.rewrite.RewriteRule;

/**
 * Service Implementation for managing {@link Chair}.
 */
@Transactional
@Service
public class ChairServiceImpl implements ChairService {

    private final Logger log = LoggerFactory.getLogger(ChairServiceImpl.class);

    private final ChairRepository chairRepository;

    private final ChairMapper chairMapper;

    public ChairServiceImpl(ChairRepository chairRepository, ChairMapper chairMapper) {
        this.chairRepository = chairRepository;
        this.chairMapper = chairMapper;
    }

    @Override
    public ChairDTO save(ChairDTO chairDTO) {
        log.debug("Request to save Chair : {}", chairDTO);
        Chair chair = chairMapper.toEntity(chairDTO);
        chair = chairRepository.save(chair);
        return chairMapper.toDto(chair);
    }

    @Override
    public Optional<ChairDTO> partialUpdate(ChairDTO chairDTO) {
        log.debug("Request to partially update Chair : {}", chairDTO);

        return chairRepository
            .findById(chairDTO.getId())
            .map(existingChair -> {
                chairMapper.partialUpdate(existingChair, chairDTO);

                return existingChair;
            })
            .map(chairRepository::save)
            .map(chairMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChairDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Chairs");
        return chairRepository.findAll(pageable).map(chairMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChairDTO> findOne(Long id) {
        log.debug("Request to get Chair : {}", id);
        return chairRepository.findById(id).map(chairMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Chair : {}", id);
        chairRepository.deleteById(id);
    }

    @Override
    public List<Chair> bookChair(List<Chair> chairs) {
        Chair auxiliChair;
        System.out.println("Entro");
        try{
            for (Chair chair : chairs) {
                System.out.println("Entro");
                System.out.println(chair.getId());

                auxiliChair=chairRepository.getById(chair.getId());
                System.out.println(auxiliChair);
                if(auxiliChair.getAvaible_chair()){
                    chair.setAvaible_chair(false);
                    System.out.println(chair);
                }
                else{
                    return null;
                }
                
            }
            chairRepository.saveAll(chairs);
            return chairs;
        }
        catch(Exception e){
            System.out.println(e.toString());
            return null;
        }
    }

    @Override
    public boolean freeChair(List<Chair> chairs) {
        try{
            for (Chair chair : chairs) {
                chair.setAvaible_chair(true);
                chair.setBooking(null); 
            }
            chairRepository.saveAll(chairs);
            return true;
        }
        catch(Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean bookChairIntoLog(List<Chair> chairs, Booking booking) {
    try{
        for (Chair chair : chairs) {
            chair.setBooking(booking);
        }
        chairRepository.saveAll(chairs);
        return true;
    }
    catch(Exception e){
        System.out.println(e.toString());
        return false;
    }

    }

    @Override
    public List<List<Chair>> getByMovieFunction(Moviefunction moviefunction) {
   
        List<Chair> listOfUnsortedChairs=chairRepository.getChairsFromMF(moviefunction);
        if(listOfUnsortedChairs.isEmpty()){return null;}
        return sortCahirsByRow(listOfUnsortedChairs);
    }
    private List<List<Chair>> sortCahirsByRow(List<Chair> unsortChairs){
        List<Chair> auxliarList;
        List<List<Chair>> sortedChairs=new ArrayList<List<Chair>>();
        int rows=unsortChairs.get(0).getMoviefunction().getHall().getRows_hall();
        int cols=unsortChairs.get(0).getMoviefunction().getHall().getCols_hall();

        for(int i=0;i<rows;i++){
            auxliarList=unsortChairs.subList(i*cols, (i+1)*cols);
            sortedChairs.add(auxliarList);
        }
        return sortedChairs;
    }

    @Override
    public List<ChairDTO> getByBooking(Booking booking) {
        List<Chair> chairs= chairRepository.getChairsFromBooking(booking);
        UtilMapper utilMapper=new UtilMapper();
        return utilMapper.convertModelToDTO(chairs);
    }
    
}
