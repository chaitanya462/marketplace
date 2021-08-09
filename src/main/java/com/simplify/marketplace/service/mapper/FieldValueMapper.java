package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.FieldValueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FieldValue} and its DTO {@link FieldValueDTO}.
 */
@Mapper(componentModel = "spring", uses = { JobPreferenceMapper.class, FieldMapper.class })
public interface FieldValueMapper extends EntityMapper<FieldValueDTO, FieldValue> {
    @Mapping(target = "jobpreference", source = "jobpreference", qualifiedByName = "id")
    @Mapping(target = "field", source = "field", qualifiedByName = "id")
    FieldValueDTO toDto(FieldValue s);
}
