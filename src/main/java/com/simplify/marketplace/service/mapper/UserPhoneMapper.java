package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.UserPhoneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserPhone} and its DTO {@link UserPhoneDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomUserMapper.class })
public interface UserPhoneMapper extends EntityMapper<UserPhoneDTO, UserPhone> {
    @Mapping(target = "customUser", source = "customUser", qualifiedByName = "id")
    UserPhoneDTO toDto(UserPhone s);
}
