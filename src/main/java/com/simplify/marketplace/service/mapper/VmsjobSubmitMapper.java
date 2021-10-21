package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.VmsjobSubmitDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VmsjobSubmit} and its DTO {@link VmsjobSubmitDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VmsjobSubmitMapper extends EntityMapper<VmsjobSubmitDTO, VmsjobSubmit> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<VmsjobSubmitDTO> toDtoIdSet(Set<VmsjobSubmit> vmsjobSubmit);
}
