package com.simplify.marketplace.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubjectMasterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubjectMaster.class);
        SubjectMaster subjectMaster1 = new SubjectMaster();
        subjectMaster1.setId(1L);
        SubjectMaster subjectMaster2 = new SubjectMaster();
        subjectMaster2.setId(subjectMaster1.getId());
        assertThat(subjectMaster1).isEqualTo(subjectMaster2);
        subjectMaster2.setId(2L);
        assertThat(subjectMaster1).isNotEqualTo(subjectMaster2);
        subjectMaster1.setId(null);
        assertThat(subjectMaster1).isNotEqualTo(subjectMaster2);
    }
}
