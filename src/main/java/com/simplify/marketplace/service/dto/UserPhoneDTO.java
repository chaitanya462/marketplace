package com.simplify.marketplace.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.UserPhone} entity.
 */
@Data
public class UserPhoneDTO implements Serializable {

    private Long id;

    private String phone;

    private Boolean isActive;

    private Boolean isPrimary;

    private String tag;

    private UserDTO user;

    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;
}
