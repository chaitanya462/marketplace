package com.simplify.marketplace.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.Address} entity.
 */
@Data
public class AddressDTO implements Serializable {

    private Long id;

    private String line1;

    private String line2;

    private String tag;

    private LocationDTO location;

    private UserDTO user;

    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;
}
