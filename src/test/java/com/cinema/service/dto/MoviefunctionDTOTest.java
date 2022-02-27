package com.cinema.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.cinema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoviefunctionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoviefunctionDTO.class);
        MoviefunctionDTO moviefunctionDTO1 = new MoviefunctionDTO();
        moviefunctionDTO1.setId(1L);
        MoviefunctionDTO moviefunctionDTO2 = new MoviefunctionDTO();
        assertThat(moviefunctionDTO1).isNotEqualTo(moviefunctionDTO2);
        moviefunctionDTO2.setId(moviefunctionDTO1.getId());
        assertThat(moviefunctionDTO1).isEqualTo(moviefunctionDTO2);
        moviefunctionDTO2.setId(2L);
        assertThat(moviefunctionDTO1).isNotEqualTo(moviefunctionDTO2);
        moviefunctionDTO1.setId(null);
        assertThat(moviefunctionDTO1).isNotEqualTo(moviefunctionDTO2);
    }
}
