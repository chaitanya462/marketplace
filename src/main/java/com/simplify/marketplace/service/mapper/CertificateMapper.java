package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.CertificateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Certificate} and its DTO {@link CertificateDTO}.
 */
@Mapper(componentModel = "spring", uses = { WorkerMapper.class })
public interface CertificateMapper extends EntityMapper<CertificateDTO, Certificate> {
    @Mapping(target = "worker", source = "worker", qualifiedByName = "id")
    CertificateDTO toDto(Certificate s);
}
