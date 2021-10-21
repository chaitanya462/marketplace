package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.VmsjobSaveDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VmsjobSave} and its DTO {@link VmsjobSaveDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VmsjobSaveMapper extends EntityMapper<VmsjobSaveDTO, VmsjobSave> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<VmsjobSaveDTO> toDtoIdSet(Set<VmsjobSave> vmsjobSave);
}
