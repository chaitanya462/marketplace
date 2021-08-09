package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.CustomUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomUser} and its DTO {@link CustomUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomUserMapper extends EntityMapper<CustomUserDTO, CustomUser> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomUserDTO toDtoId(CustomUser customUser);
}
