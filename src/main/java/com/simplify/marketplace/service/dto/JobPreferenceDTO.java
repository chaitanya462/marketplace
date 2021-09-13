package com.simplify.marketplace.service.dto;

import com.simplify.marketplace.domain.enumeration.EmploymentType;
import com.simplify.marketplace.domain.enumeration.EngagementType;
import com.simplify.marketplace.domain.enumeration.LocationType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.JobPreference} entity.
 */
@Data
public class JobPreferenceDTO implements Serializable {

    private Long id;

    private Integer hourlyRate;

    private Integer dailyRate;

    private Integer monthlyRate;

    private Integer hourPerDay;

    private Integer hourPerWeek;

    private EngagementType engagementType;

    private EmploymentType employmentType;

    private LocationType locationType;

    private LocalDate availableFrom;

    private LocalDate availableTo;

    private Boolean isActive;

    private CategoryDTO subCategory;

    private WorkerDTO worker;

    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;
}
