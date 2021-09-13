package com.simplify.marketplace.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OtpAttemptDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OtpAttemptDTO.class);
        OtpAttemptDTO otpAttemptDTO1 = new OtpAttemptDTO();
        otpAttemptDTO1.setId(1L);
        OtpAttemptDTO otpAttemptDTO2 = new OtpAttemptDTO();
        assertThat(otpAttemptDTO1).isNotEqualTo(otpAttemptDTO2);
        otpAttemptDTO2.setId(otpAttemptDTO1.getId());
        assertThat(otpAttemptDTO1).isEqualTo(otpAttemptDTO2);
        otpAttemptDTO2.setId(2L);
        assertThat(otpAttemptDTO1).isNotEqualTo(otpAttemptDTO2);
        otpAttemptDTO1.setId(null);
        assertThat(otpAttemptDTO1).isNotEqualTo(otpAttemptDTO2);
    }
}
