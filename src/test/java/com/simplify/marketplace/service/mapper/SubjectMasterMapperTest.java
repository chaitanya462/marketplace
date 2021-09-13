package com.simplify.marketplace.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubjectMasterMapperTest {

    private SubjectMasterMapper subjectMasterMapper;

    @BeforeEach
    public void setUp() {
        subjectMasterMapper = new SubjectMasterMapperImpl();
    }
}
