package com.simplify.marketplace.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SkillsMasterMapperTest {

    private SkillsMasterMapper skillsMasterMapper;

    @BeforeEach
    public void setUp() {
        skillsMasterMapper = new SkillsMasterMapperImpl();
    }
}
