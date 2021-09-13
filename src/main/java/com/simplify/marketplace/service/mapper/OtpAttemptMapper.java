package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.OtpAttemptDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OtpAttempt} and its DTO {@link OtpAttemptDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OtpAttemptMapper extends EntityMapper<OtpAttemptDTO, OtpAttempt> {}
