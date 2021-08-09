package com.simplify.marketplace.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocationPrefrenceMapperTest {

    private LocationPrefrenceMapper locationPrefrenceMapper;

    @BeforeEach
    public void setUp() {
        locationPrefrenceMapper = new LocationPrefrenceMapperImpl();
    }
}
