package com.simplify.marketplace.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmploymentMapperTest {

    private EmploymentMapper employmentMapper;

    @BeforeEach
    public void setUp() {
        employmentMapper = new EmploymentMapperImpl();
    }
}
