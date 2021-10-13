package com.simplify.marketplace.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.marketplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VmsjobSaveDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VmsjobSaveDTO.class);
        VmsjobSaveDTO vmsjobSaveDTO1 = new VmsjobSaveDTO();
        vmsjobSaveDTO1.setId(1L);
        VmsjobSaveDTO vmsjobSaveDTO2 = new VmsjobSaveDTO();
        assertThat(vmsjobSaveDTO1).isNotEqualTo(vmsjobSaveDTO2);
        vmsjobSaveDTO2.setId(vmsjobSaveDTO1.getId());
        assertThat(vmsjobSaveDTO1).isEqualTo(vmsjobSaveDTO2);
        vmsjobSaveDTO2.setId(2L);
        assertThat(vmsjobSaveDTO1).isNotEqualTo(vmsjobSaveDTO2);
        vmsjobSaveDTO1.setId(null);
        assertThat(vmsjobSaveDTO1).isNotEqualTo(vmsjobSaveDTO2);
    }
}
