package com.simplify.marketplace.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserEmailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserEmail.class);
        UserEmail userEmail1 = new UserEmail();
        userEmail1.setId(1L);
        UserEmail userEmail2 = new UserEmail();
        userEmail2.setId(userEmail1.getId());
        assertThat(userEmail1).isEqualTo(userEmail2);
        userEmail2.setId(2L);
        assertThat(userEmail1).isNotEqualTo(userEmail2);
        userEmail1.setId(null);
        assertThat(userEmail1).isNotEqualTo(userEmail2);
    }
}
