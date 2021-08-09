package com.simplify.marketplace.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubjectMasterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubjectMasterDTO.class);
        SubjectMasterDTO subjectMasterDTO1 = new SubjectMasterDTO();
        subjectMasterDTO1.setId(1L);
        SubjectMasterDTO subjectMasterDTO2 = new SubjectMasterDTO();
        assertThat(subjectMasterDTO1).isNotEqualTo(subjectMasterDTO2);
        subjectMasterDTO2.setId(subjectMasterDTO1.getId());
        assertThat(subjectMasterDTO1).isEqualTo(subjectMasterDTO2);
        subjectMasterDTO2.setId(2L);
        assertThat(subjectMasterDTO1).isNotEqualTo(subjectMasterDTO2);
        subjectMasterDTO1.setId(null);
        assertThat(subjectMasterDTO1).isNotEqualTo(subjectMasterDTO2);
    }
}
