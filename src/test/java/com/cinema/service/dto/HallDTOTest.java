package com.cinema.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.cinema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HallDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HallDTO.class);
        HallDTO hallDTO1 = new HallDTO();
        hallDTO1.setId(1L);
        HallDTO hallDTO2 = new HallDTO();
        assertThat(hallDTO1).isNotEqualTo(hallDTO2);
        hallDTO2.setId(hallDTO1.getId());
        assertThat(hallDTO1).isEqualTo(hallDTO2);
        hallDTO2.setId(2L);
        assertThat(hallDTO1).isNotEqualTo(hallDTO2);
        hallDTO1.setId(null);
        assertThat(hallDTO1).isNotEqualTo(hallDTO2);
    }
}
