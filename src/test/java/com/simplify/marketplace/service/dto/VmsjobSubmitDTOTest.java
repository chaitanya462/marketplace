package com.simplify.marketplace.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VmsjobSubmitDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VmsjobSubmitDTO.class);
        VmsjobSubmitDTO vmsjobSubmitDTO1 = new VmsjobSubmitDTO();
        vmsjobSubmitDTO1.setId(1L);
        VmsjobSubmitDTO vmsjobSubmitDTO2 = new VmsjobSubmitDTO();
        assertThat(vmsjobSubmitDTO1).isNotEqualTo(vmsjobSubmitDTO2);
        vmsjobSubmitDTO2.setId(vmsjobSubmitDTO1.getId());
        assertThat(vmsjobSubmitDTO1).isEqualTo(vmsjobSubmitDTO2);
        vmsjobSubmitDTO2.setId(2L);
        assertThat(vmsjobSubmitDTO1).isNotEqualTo(vmsjobSubmitDTO2);
        vmsjobSubmitDTO1.setId(null);
        assertThat(vmsjobSubmitDTO1).isNotEqualTo(vmsjobSubmitDTO2);
    }
}
