package com.cinema.service.mapper;

import java.util.List;

import com.cinema.domain.Chair;
import com.cinema.service.dto.ChairDTO;

import java.lang.reflect.Type;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

public class UtilMapper {
    public List<ChairDTO>convertModelToDTO(List<Chair> chairs){
        
        ModelMapper modelMapper = new ModelMapper();
		Type listType = new TypeToken<List<ChairDTO>>(){}.getType();
		List<ChairDTO> chairsDTO = modelMapper.map(chairs, listType);
		return chairsDTO;
    }
    public List<Chair>convertDTOtoModel(List<ChairDTO> chairDTOs){
        ModelMapper modelMapper= new ModelMapper();
        Type listType= new TypeToken<List<Chair>>(){}.getType();
        List<Chair> chairs= modelMapper.map(chairDTOs, listType);
        return chairs;
    }
}
