package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.FileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link File} and its DTO {@link FileDTO}.
 */
@Mapper(componentModel = "spring", uses = { WorkerMapper.class })
public interface FileMapper extends EntityMapper<FileDTO, File> {
    @Mapping(target = "worker", source = "worker", qualifiedByName = "id")
    FileDTO toDto(File s);
}
