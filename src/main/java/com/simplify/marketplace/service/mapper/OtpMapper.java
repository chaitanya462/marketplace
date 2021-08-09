package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.OtpDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Otp} and its DTO {@link OtpDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OtpMapper extends EntityMapper<OtpDTO, Otp> {}
