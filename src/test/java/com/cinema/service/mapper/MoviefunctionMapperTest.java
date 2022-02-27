package com.cinema.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoviefunctionMapperTest {

    private MoviefunctionMapper moviefunctionMapper;

    @BeforeEach
    public void setUp() {
        moviefunctionMapper = new MoviefunctionMapperImpl();
    }
}
