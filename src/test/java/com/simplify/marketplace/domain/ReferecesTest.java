package com.simplify.marketplace.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReferecesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Refereces.class);
        Refereces refereces1 = new Refereces();
        refereces1.setId(1L);
        Refereces refereces2 = new Refereces();
        refereces2.setId(refereces1.getId());
        assertThat(refereces1).isEqualTo(refereces2);
        refereces2.setId(2L);
        assertThat(refereces1).isNotEqualTo(refereces2);
        refereces1.setId(null);
        assertThat(refereces1).isNotEqualTo(refereces2);
    }
}
