package com.simplify.marketplace.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.SubjectMaster} entity.
 */
@Data
public class SubjectMasterDTO implements Serializable {

    private Long id;

    private String subjectName;

    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;
}
