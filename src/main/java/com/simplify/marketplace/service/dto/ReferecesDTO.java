package com.simplify.marketplace.service.dto;

import com.simplify.marketplace.domain.enumeration.RelationType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.Refereces} entity.
 */
@Data
public class ReferecesDTO implements Serializable {

    private Long id;

    private String name;

    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    private String email;

    private String phone;

    private String profileLink;

    private RelationType relationType;

    private WorkerDTO worker;

    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;
}
