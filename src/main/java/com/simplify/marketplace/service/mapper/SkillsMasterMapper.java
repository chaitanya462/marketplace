package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.SkillsMasterDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SkillsMaster} and its DTO {@link SkillsMasterDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SkillsMasterMapper extends EntityMapper<SkillsMasterDTO, SkillsMaster> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<SkillsMasterDTO> toDtoIdSet(Set<SkillsMaster> skillsMaster);
}
