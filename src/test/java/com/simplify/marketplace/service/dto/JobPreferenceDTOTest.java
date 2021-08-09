package com.simplify.marketplace.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JobPreferenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobPreferenceDTO.class);
        JobPreferenceDTO jobPreferenceDTO1 = new JobPreferenceDTO();
        jobPreferenceDTO1.setId(1L);
        JobPreferenceDTO jobPreferenceDTO2 = new JobPreferenceDTO();
        assertThat(jobPreferenceDTO1).isNotEqualTo(jobPreferenceDTO2);
        jobPreferenceDTO2.setId(jobPreferenceDTO1.getId());
        assertThat(jobPreferenceDTO1).isEqualTo(jobPreferenceDTO2);
        jobPreferenceDTO2.setId(2L);
        assertThat(jobPreferenceDTO1).isNotEqualTo(jobPreferenceDTO2);
        jobPreferenceDTO1.setId(null);
        assertThat(jobPreferenceDTO1).isNotEqualTo(jobPreferenceDTO2);
    }
}
