package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.ReferecesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Refereces} and its DTO {@link ReferecesDTO}.
 */
@Mapper(componentModel = "spring", uses = { WorkerMapper.class })
public interface ReferecesMapper extends EntityMapper<ReferecesDTO, Refereces> {
    @Mapping(target = "worker", source = "worker", qualifiedByName = "id")
    ReferecesDTO toDto(Refereces s);
}
