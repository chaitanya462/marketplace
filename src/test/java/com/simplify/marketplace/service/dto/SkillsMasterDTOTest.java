package com.simplify.marketplace.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SkillsMasterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkillsMasterDTO.class);
        SkillsMasterDTO skillsMasterDTO1 = new SkillsMasterDTO();
        skillsMasterDTO1.setId(1L);
        SkillsMasterDTO skillsMasterDTO2 = new SkillsMasterDTO();
        assertThat(skillsMasterDTO1).isNotEqualTo(skillsMasterDTO2);
        skillsMasterDTO2.setId(skillsMasterDTO1.getId());
        assertThat(skillsMasterDTO1).isEqualTo(skillsMasterDTO2);
        skillsMasterDTO2.setId(2L);
        assertThat(skillsMasterDTO1).isNotEqualTo(skillsMasterDTO2);
        skillsMasterDTO1.setId(null);
        assertThat(skillsMasterDTO1).isNotEqualTo(skillsMasterDTO2);
    }
}
