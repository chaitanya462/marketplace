package com.simplify.marketplace.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationPrefrenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationPrefrenceDTO.class);
        LocationPrefrenceDTO locationPrefrenceDTO1 = new LocationPrefrenceDTO();
        locationPrefrenceDTO1.setId(1L);
        LocationPrefrenceDTO locationPrefrenceDTO2 = new LocationPrefrenceDTO();
        assertThat(locationPrefrenceDTO1).isNotEqualTo(locationPrefrenceDTO2);
        locationPrefrenceDTO2.setId(locationPrefrenceDTO1.getId());
        assertThat(locationPrefrenceDTO1).isEqualTo(locationPrefrenceDTO2);
        locationPrefrenceDTO2.setId(2L);
        assertThat(locationPrefrenceDTO1).isNotEqualTo(locationPrefrenceDTO2);
        locationPrefrenceDTO1.setId(null);
        assertThat(locationPrefrenceDTO1).isNotEqualTo(locationPrefrenceDTO2);
    }
}
