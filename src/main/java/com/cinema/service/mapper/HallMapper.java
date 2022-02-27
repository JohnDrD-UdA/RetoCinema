package com.cinema.service.mapper;

import com.cinema.domain.Hall;
import com.cinema.service.dto.HallDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Hall} and its DTO {@link HallDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HallMapper extends EntityMapper<HallDTO, Hall> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HallDTO toDtoId(Hall hall);
}
