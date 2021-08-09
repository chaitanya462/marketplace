package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.SubjectMasterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubjectMaster} and its DTO {@link SubjectMasterDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubjectMasterMapper extends EntityMapper<SubjectMasterDTO, SubjectMaster> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubjectMasterDTO toDtoId(SubjectMaster subjectMaster);
}
