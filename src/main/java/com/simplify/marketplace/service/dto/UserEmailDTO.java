package com.simplify.marketplace.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.UserEmail} entity.
 */
@Data
public class UserEmailDTO implements Serializable {

    private Long id;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    private String email;

    private Boolean isActive;

    private Boolean isPrimary;

    private String tag;

    private UserDTO user;

    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;
}
