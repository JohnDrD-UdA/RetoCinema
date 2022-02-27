package com.cinema.service.mapper;

import com.cinema.domain.Moviefunction;
import com.cinema.service.dto.MoviefunctionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Moviefunction} and its DTO {@link MoviefunctionDTO}.
 */
@Mapper(componentModel = "spring", uses = { MovieMapper.class, HallMapper.class })
public interface MoviefunctionMapper extends EntityMapper<MoviefunctionDTO, Moviefunction> {
    @Mapping(target = "movie", source = "movie", qualifiedByName = "id")
    @Mapping(target = "hall", source = "hall", qualifiedByName = "id")
    MoviefunctionDTO toDto(Moviefunction s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MoviefunctionDTO toDtoId(Moviefunction moviefunction);
}
