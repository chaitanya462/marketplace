package com.simplify.marketplace.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VmsjobSaveMapperTest {

    private VmsjobSaveMapper vmsjobSaveMapper;

    @BeforeEach
    public void setUp() {
        vmsjobSaveMapper = new VmsjobSaveMapperImpl();
    }
}
