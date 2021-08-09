package com.simplify.marketplace.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OtpMapperTest {

    private OtpMapper otpMapper;

    @BeforeEach
    public void setUp() {
        otpMapper = new OtpMapperImpl();
    }
}
