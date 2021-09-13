package com.simplify.marketplace.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.Employment} entity.
 */
@Data
public class EmploymentDTO implements Serializable {

    private Long id;

    private String jobTitle;

    private String companyName;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isCurrent;

    private Integer lastSalary;

    private String description;

    private String employeeLocations;

    private ClientDTO company;

    private WorkerDTO worker;

    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;
}
