package com.simplify.marketplace.service.mapper;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.service.dto.UserEmailDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserEmail} and its DTO {@link UserEmailDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface UserEmailMapper extends EntityMapper<UserEmailDTO, UserEmail> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    UserEmailDTO toDto(UserEmail s);
}
