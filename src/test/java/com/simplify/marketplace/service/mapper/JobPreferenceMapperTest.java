package com.simplify.marketplace.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JobPreferenceMapperTest {

    private JobPreferenceMapper jobPreferenceMapper;

    @BeforeEach
    public void setUp() {
        jobPreferenceMapper = new JobPreferenceMapperImpl();
    }
}
