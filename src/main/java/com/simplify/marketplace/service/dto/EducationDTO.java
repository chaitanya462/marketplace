package com.simplify.marketplace.service.dto;

import com.simplify.marketplace.domain.enumeration.DegreeType;
import com.simplify.marketplace.domain.enumeration.EducationGrade;
import com.simplify.marketplace.domain.enumeration.MarksType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.Education} entity.
 */
@Data
public class EducationDTO implements Serializable {

    private Long id;

    private String degreeName;

    private String institute;

    private Integer yearOfPassing;

    private Float marks;

    private MarksType marksType;

    private EducationGrade grade;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isComplete;

    private DegreeType degreeType;

    private String description;

    private SubjectMasterDTO majorSubject;

    private SubjectMasterDTO minorSubject;

    private WorkerDTO worker;

    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;
}
