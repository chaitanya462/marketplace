package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.FieldDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Field} and its DTO {@link FieldDTO}.
 */
@Mapper(componentModel = "spring", uses = { CategoryMapper.class })
public interface FieldMapper extends EntityMapper<FieldDTO, Field> {
    @Mapping(target = "category", source = "category", qualifiedByName = "id")
    FieldDTO toDto(Field s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FieldDTO toDtoId(Field field);
}
