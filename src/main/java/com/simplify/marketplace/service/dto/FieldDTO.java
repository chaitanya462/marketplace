package com.simplify.marketplace.service.dto;

import com.simplify.marketplace.domain.enumeration.FieldType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.Field} entity.
 */
@Data
public class FieldDTO implements Serializable {

    private Long id;

    private String fieldName;

    private String fieldLabel;

    private FieldType fieldType;

    private Boolean isActive;

    private CategoryDTO category;

    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;
}
