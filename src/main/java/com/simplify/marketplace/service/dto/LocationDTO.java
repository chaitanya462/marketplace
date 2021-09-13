package com.simplify.marketplace.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.Location} entity.
 */
@Data
public class LocationDTO implements Serializable {

    private Long id;

    @Min(value = 10000)
    @Max(value = 99999)
    private Integer pincode;

    private String country;

    private String state;

    private String city;

    private EmploymentDTO employment;

    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;
}
