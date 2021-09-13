package com.simplify.marketplace.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDate;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.Worker} entity.
 */
@Data
public class WorkerDTO implements Serializable {

    private Long id;

    @NotNull
    private String firstName;

    private String middleName;

    @NotNull
    private String lastName;

    private String email;

    private String gender;

    private String language;

    private String status;

    private String idCode;

    private String idProof;

    private String workerLocation;

    private String primaryPhone;

    private String description;

    private LocalDate dateOfBirth;

    private Boolean isActive;

    private UserDTO user;
    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;

    private Set<SkillsMasterDTO> skills = new HashSet<>();
}
