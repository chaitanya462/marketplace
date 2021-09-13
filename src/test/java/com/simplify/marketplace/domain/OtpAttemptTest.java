package com.simplify.marketplace.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OtpAttemptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OtpAttempt.class);
        OtpAttempt otpAttempt1 = new OtpAttempt();
        otpAttempt1.setId(1L);
        OtpAttempt otpAttempt2 = new OtpAttempt();
        otpAttempt2.setId(otpAttempt1.getId());
        assertThat(otpAttempt1).isEqualTo(otpAttempt2);
        otpAttempt2.setId(2L);
        assertThat(otpAttempt1).isNotEqualTo(otpAttempt2);
        otpAttempt1.setId(null);
        assertThat(otpAttempt1).isNotEqualTo(otpAttempt2);
    }
}
