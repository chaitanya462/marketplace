package com.simplify.marketplace.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OtpTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Otp.class);
        Otp otp1 = new Otp();
        otp1.setId(1L);
        Otp otp2 = new Otp();
        otp2.setId(otp1.getId());
        assertThat(otp1).isEqualTo(otp2);
        otp2.setId(2L);
        assertThat(otp1).isNotEqualTo(otp2);
        otp1.setId(null);
        assertThat(otp1).isNotEqualTo(otp2);
    }
}
