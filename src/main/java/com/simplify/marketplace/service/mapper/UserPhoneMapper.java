package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.UserPhoneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserPhone} and its DTO {@link UserPhoneDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface UserPhoneMapper extends EntityMapper<UserPhoneDTO, UserPhone> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    UserPhoneDTO toDto(UserPhone s);
}
