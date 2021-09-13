package com.simplify.marketplace.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserEmailMapperTest {

    private UserEmailMapper userEmailMapper;

    @BeforeEach
    public void setUp() {
        userEmailMapper = new UserEmailMapperImpl();
    }
}
