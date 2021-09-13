package com.simplify.marketplace.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.FieldValue} entity.
 */
@Data
public class FieldValueDTO implements Serializable {

    private Long id;

    private String value;

    private JobPreferenceDTO jobpreference;

    private FieldDTO field;

    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;
}
