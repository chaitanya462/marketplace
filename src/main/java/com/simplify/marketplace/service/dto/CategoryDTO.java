package com.simplify.marketplace.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.Category} entity.
 */
@Data
public class CategoryDTO implements Serializable {

    private Long id;

    private String name;

    private Boolean isParent;

    private Boolean isActive;

    private CategoryDTO parent;

    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;
}
