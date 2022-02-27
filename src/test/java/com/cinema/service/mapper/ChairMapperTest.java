package com.cinema.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChairMapperTest {

    private ChairMapper chairMapper;

    @BeforeEach
    public void setUp() {
        chairMapper = new ChairMapperImpl();
    }
}
