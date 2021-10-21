package com.simplify.marketplace.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VmsjobSubmitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VmsjobSubmit.class);
        VmsjobSubmit vmsjobSubmit1 = new VmsjobSubmit();
        vmsjobSubmit1.setId(1L);
        VmsjobSubmit vmsjobSubmit2 = new VmsjobSubmit();
        vmsjobSubmit2.setId(vmsjobSubmit1.getId());
        assertThat(vmsjobSubmit1).isEqualTo(vmsjobSubmit2);
        vmsjobSubmit2.setId(2L);
        assertThat(vmsjobSubmit1).isNotEqualTo(vmsjobSubmit2);
        vmsjobSubmit1.setId(null);
        assertThat(vmsjobSubmit1).isNotEqualTo(vmsjobSubmit2);
    }
}
