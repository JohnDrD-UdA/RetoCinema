package com.cinema.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HallMapperTest {

    private HallMapper hallMapper;

    @BeforeEach
    public void setUp() {
        hallMapper = new HallMapperImpl();
    }
}
