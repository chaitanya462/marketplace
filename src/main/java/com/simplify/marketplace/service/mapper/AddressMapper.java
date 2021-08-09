package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.AddressDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = { LocationMapper.class, CustomUserMapper.class })
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {
    @Mapping(target = "location", source = "location", qualifiedByName = "id")
    @Mapping(target = "customUser", source = "customUser", qualifiedByName = "id")
    AddressDTO toDto(Address s);
}
