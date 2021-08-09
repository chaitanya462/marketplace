package com.simplify.marketplace.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserEmailDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserEmailDTO.class);
        UserEmailDTO userEmailDTO1 = new UserEmailDTO();
        userEmailDTO1.setId(1L);
        UserEmailDTO userEmailDTO2 = new UserEmailDTO();
        assertThat(userEmailDTO1).isNotEqualTo(userEmailDTO2);
        userEmailDTO2.setId(userEmailDTO1.getId());
        assertThat(userEmailDTO1).isEqualTo(userEmailDTO2);
        userEmailDTO2.setId(2L);
        assertThat(userEmailDTO1).isNotEqualTo(userEmailDTO2);
        userEmailDTO1.setId(null);
        assertThat(userEmailDTO1).isNotEqualTo(userEmailDTO2);
    }
}
