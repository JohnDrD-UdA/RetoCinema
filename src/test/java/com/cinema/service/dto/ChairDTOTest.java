package com.cinema.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.cinema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChairDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChairDTO.class);
        ChairDTO chairDTO1 = new ChairDTO();
        chairDTO1.setId(1L);
        ChairDTO chairDTO2 = new ChairDTO();
        assertThat(chairDTO1).isNotEqualTo(chairDTO2);
        chairDTO2.setId(chairDTO1.getId());
        assertThat(chairDTO1).isEqualTo(chairDTO2);
        chairDTO2.setId(2L);
        assertThat(chairDTO1).isNotEqualTo(chairDTO2);
        chairDTO1.setId(null);
        assertThat(chairDTO1).isNotEqualTo(chairDTO2);
    }
}
