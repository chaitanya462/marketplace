package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.WorkerDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Worker} and its DTO {@link WorkerDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, SkillsMasterMapper.class })
public interface WorkerMapper extends EntityMapper<WorkerDTO, Worker> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    @Mapping(target = "skills", source = "skills", qualifiedByName = "idSet")
    WorkerDTO toDto(Worker s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkerDTO toDtoId(Worker worker);

    @Mapping(target = "removeSkill", ignore = true)
    Worker toEntity(WorkerDTO workerDTO);
}
