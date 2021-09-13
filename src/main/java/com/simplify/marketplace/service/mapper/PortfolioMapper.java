package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.PortfolioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Portfolio} and its DTO {@link PortfolioDTO}.
 */
@Mapper(componentModel = "spring", uses = { WorkerMapper.class })
public interface PortfolioMapper extends EntityMapper<PortfolioDTO, Portfolio> {
    @Mapping(target = "worker", source = "worker", qualifiedByName = "id")
    PortfolioDTO toDto(Portfolio s);
}
