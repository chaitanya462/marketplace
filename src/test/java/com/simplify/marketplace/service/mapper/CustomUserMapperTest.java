package com.simplify.marketplace.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomUserMapperTest {

    private CustomUserMapper customUserMapper;

    @BeforeEach
    public void setUp() {
        customUserMapper = new CustomUserMapperImpl();
    }
}
