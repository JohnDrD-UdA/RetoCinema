package com.cinema.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cinema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoviefunctionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Moviefunction.class);
        Moviefunction moviefunction1 = new Moviefunction();
        moviefunction1.setId(1L);
        Moviefunction moviefunction2 = new Moviefunction();
        moviefunction2.setId(moviefunction1.getId());
        assertThat(moviefunction1).isEqualTo(moviefunction2);
        moviefunction2.setId(2L);
        assertThat(moviefunction1).isNotEqualTo(moviefunction2);
        moviefunction1.setId(null);
        assertThat(moviefunction1).isNotEqualTo(moviefunction2);
    }
}
