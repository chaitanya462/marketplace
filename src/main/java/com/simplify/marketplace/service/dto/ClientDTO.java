package com.simplify.marketplace.service.dto;

import com.simplify.marketplace.domain.enumeration.CompanyType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.Client} entity.
 */
@Data
public class ClientDTO implements Serializable {

    private Long id;

    private String companyName;

    private String companyWebsite;

    private CompanyType companyType;

    private String primaryPhone;

    private Boolean isActive;

    private String description;

    private LocalDate startDate;

    private UserDTO user;

    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;
}
