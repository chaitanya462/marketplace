package com.simplify.marketplace.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VmsjobSaveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VmsjobSave.class);
        VmsjobSave vmsjobSave1 = new VmsjobSave();
        vmsjobSave1.setId(1L);
        VmsjobSave vmsjobSave2 = new VmsjobSave();
        vmsjobSave2.setId(vmsjobSave1.getId());
        assertThat(vmsjobSave1).isEqualTo(vmsjobSave2);
        vmsjobSave2.setId(2L);
        assertThat(vmsjobSave1).isNotEqualTo(vmsjobSave2);
        vmsjobSave1.setId(null);
        assertThat(vmsjobSave1).isNotEqualTo(vmsjobSave2);
    }
}
