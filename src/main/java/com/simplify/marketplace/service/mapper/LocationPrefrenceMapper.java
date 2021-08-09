package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.LocationPrefrenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LocationPrefrence} and its DTO {@link LocationPrefrenceDTO}.
 */
@Mapper(componentModel = "spring", uses = { JobPreferenceMapper.class, LocationMapper.class })
public interface LocationPrefrenceMapper extends EntityMapper<LocationPrefrenceDTO, LocationPrefrence> {
    @Mapping(target = "worker", source = "worker", qualifiedByName = "id")
    @Mapping(target = "location", source = "location", qualifiedByName = "id")
    LocationPrefrenceDTO toDto(LocationPrefrence s);
}
