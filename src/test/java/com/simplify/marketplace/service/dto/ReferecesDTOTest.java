package com.simplify.marketplace.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReferecesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReferecesDTO.class);
        ReferecesDTO referecesDTO1 = new ReferecesDTO();
        referecesDTO1.setId(1L);
        ReferecesDTO referecesDTO2 = new ReferecesDTO();
        assertThat(referecesDTO1).isNotEqualTo(referecesDTO2);
        referecesDTO2.setId(referecesDTO1.getId());
        assertThat(referecesDTO1).isEqualTo(referecesDTO2);
        referecesDTO2.setId(2L);
        assertThat(referecesDTO1).isNotEqualTo(referecesDTO2);
        referecesDTO1.setId(null);
        assertThat(referecesDTO1).isNotEqualTo(referecesDTO2);
    }
}
