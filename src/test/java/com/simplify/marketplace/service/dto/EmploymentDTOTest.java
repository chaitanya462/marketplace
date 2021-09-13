package com.simplify.marketplace.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmploymentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmploymentDTO.class);
        EmploymentDTO employmentDTO1 = new EmploymentDTO();
        employmentDTO1.setId(1L);
        EmploymentDTO employmentDTO2 = new EmploymentDTO();
        assertThat(employmentDTO1).isNotEqualTo(employmentDTO2);
        employmentDTO2.setId(employmentDTO1.getId());
        assertThat(employmentDTO1).isEqualTo(employmentDTO2);
        employmentDTO2.setId(2L);
        assertThat(employmentDTO1).isNotEqualTo(employmentDTO2);
        employmentDTO1.setId(null);
        assertThat(employmentDTO1).isNotEqualTo(employmentDTO2);
    }
}
