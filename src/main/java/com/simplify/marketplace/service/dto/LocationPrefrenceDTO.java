package com.simplify.marketplace.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.LocationPrefrence} entity.
 */
@Data
public class LocationPrefrenceDTO implements Serializable {

    private Long id;

    private Integer prefrenceOrder;

    private JobPreferenceDTO worker;

    private LocationDTO location;

    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;
}
