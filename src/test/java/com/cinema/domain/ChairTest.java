package com.cinema.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cinema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChairTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Chair.class);
        Chair chair1 = new Chair();
        chair1.setId(1L);
        Chair chair2 = new Chair();
        chair2.setId(chair1.getId());
        assertThat(chair1).isEqualTo(chair2);
        chair2.setId(2L);
        assertThat(chair1).isNotEqualTo(chair2);
        chair1.setId(null);
        assertThat(chair1).isNotEqualTo(chair2);
    }
}
