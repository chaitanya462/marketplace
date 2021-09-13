package com.simplify.marketplace.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserPhoneDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPhoneDTO.class);
        UserPhoneDTO userPhoneDTO1 = new UserPhoneDTO();
        userPhoneDTO1.setId(1L);
        UserPhoneDTO userPhoneDTO2 = new UserPhoneDTO();
        assertThat(userPhoneDTO1).isNotEqualTo(userPhoneDTO2);
        userPhoneDTO2.setId(userPhoneDTO1.getId());
        assertThat(userPhoneDTO1).isEqualTo(userPhoneDTO2);
        userPhoneDTO2.setId(2L);
        assertThat(userPhoneDTO1).isNotEqualTo(userPhoneDTO2);
        userPhoneDTO1.setId(null);
        assertThat(userPhoneDTO1).isNotEqualTo(userPhoneDTO2);
    }
}
