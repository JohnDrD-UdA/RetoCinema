package com.cinema.service.mapper;

import com.cinema.domain.Chair;
import com.cinema.service.dto.ChairDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Chair} and its DTO {@link ChairDTO}.
 */
@Mapper(componentModel = "spring", uses = { BookingMapper.class, MoviefunctionMapper.class })
public interface ChairMapper extends EntityMapper<ChairDTO, Chair> {
    @Mapping(target = "booking", source = "booking", qualifiedByName = "id")
    @Mapping(target = "moviefunction", source = "moviefunction", qualifiedByName = "id")
    ChairDTO toDto(Chair s);
}
