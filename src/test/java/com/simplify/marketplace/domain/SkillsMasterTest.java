package com.simplify.marketplace.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SkillsMasterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkillsMaster.class);
        SkillsMaster skillsMaster1 = new SkillsMaster();
        skillsMaster1.setId(1L);
        SkillsMaster skillsMaster2 = new SkillsMaster();
        skillsMaster2.setId(skillsMaster1.getId());
        assertThat(skillsMaster1).isEqualTo(skillsMaster2);
        skillsMaster2.setId(2L);
        assertThat(skillsMaster1).isNotEqualTo(skillsMaster2);
        skillsMaster1.setId(null);
        assertThat(skillsMaster1).isNotEqualTo(skillsMaster2);
    }
}
