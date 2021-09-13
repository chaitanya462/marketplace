package com.simplify.marketplace.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.simplify.marketplace.domain.enumeration.EmploymentType;
import com.simplify.marketplace.domain.enumeration.EngagementType;
import com.simplify.marketplace.domain.enumeration.LocationType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A JobPreference.
 */
@Entity
@Table(name = "job_preference")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class JobPreference implements Serializable {

    //    public Category getSubCategory() {
    //		return subCategory;
    //	}
    //
    //	public void setSubCategory(Category subCategory) {
    //		this.subCategory = subCategory;
    //	}

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hourly_rate")
    private Integer hourlyRate;

    @Column(name = "daily_rate")
    private Integer dailyRate;

    @Column(name = "monthly_rate")
    private Integer monthlyRate;

    @Column(name = "hour_per_day")
    private Integer hourPerDay;

    @Column(name = "hour_per_week")
    private Integer hourPerWeek;

    @Enumerated(EnumType.STRING)
    @Column(name = "engagement_type")
    private EngagementType engagementType;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type")
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_type")
    private LocationType locationType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
    @Column(name = "available_from")
    private LocalDate availableFrom;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
    @Column(name = "available_to")
    private LocalDate availableTo;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_by")
    private String createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @OneToMany(mappedBy = "worker")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "worker", "location" }, allowSetters = true)
    private Set<LocationPrefrence> locationPrefrences = new HashSet<>();

    @OneToMany(mappedBy = "jobpreference")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "jobpreference", "field" }, allowSetters = true)
    private Set<FieldValue> fieldValues = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "categories", "fields", "parent" }, allowSetters = true)
    private Category subCategory;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "user", "files", "educations", "certificates", "employments", "portfolios", "refereces", "jobPreferences", "skills" },
        allowSetters = true
    )
    private Worker worker;

    public JobPreference id(Long id) {
        this.id = id;
        return this;
    }

    public JobPreference hourlyRate(Integer hourlyRate) {
        this.hourlyRate = hourlyRate;
        return this;
    }

    public JobPreference dailyRate(Integer dailyRate) {
        this.dailyRate = dailyRate;
        return this;
    }

    public JobPreference monthlyRate(Integer monthlyRate) {
        this.monthlyRate = monthlyRate;
        return this;
    }

    public JobPreference hourPerDay(Integer hourPerDay) {
        this.hourPerDay = hourPerDay;
        return this;
    }

    public JobPreference hourPerWeek(Integer hourPerWeek) {
        this.hourPerWeek = hourPerWeek;
        return this;
    }

    public JobPreference engagementType(EngagementType engagementType) {
        this.engagementType = engagementType;
        return this;
    }

    public JobPreference employmentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
        return this;
    }

    public JobPreference locationType(LocationType locationType) {
        this.locationType = locationType;
        return this;
    }

    public JobPreference availableFrom(LocalDate availableFrom) {
        this.availableFrom = availableFrom;
        return this;
    }

    public JobPreference availableTo(LocalDate availableTo) {
        this.availableTo = availableTo;
        return this;
    }

    public JobPreference isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public JobPreference locationPrefrences(Set<LocationPrefrence> locationPrefrences) {
        this.setLocationPrefrences(locationPrefrences);
        return this;
    }

    public JobPreference addLocationPrefrence(LocationPrefrence locationPrefrence) {
        this.locationPrefrences.add(locationPrefrence);
        locationPrefrence.setWorker(this);
        return this;
    }

    public JobPreference removeLocationPrefrence(LocationPrefrence locationPrefrence) {
        this.locationPrefrences.remove(locationPrefrence);
        locationPrefrence.setWorker(null);
        return this;
    }

    public JobPreference fieldValues(Set<FieldValue> fieldValues) {
        this.setFieldValues(fieldValues);
        return this;
    }

    public JobPreference addFieldValue(FieldValue fieldValue) {
        this.fieldValues.add(fieldValue);
        fieldValue.setJobpreference(this);
        return this;
    }

    public JobPreference removeFieldValue(FieldValue fieldValue) {
        this.fieldValues.remove(fieldValue);
        fieldValue.setJobpreference(null);
        return this;
    }

    public JobPreference subCategory(Category category) {
        this.setSubCategory(category);
        return this;
    }

    public JobPreference worker(Worker worker) {
        this.setWorker(worker);
        return this;
    }

    public JobPreference createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public JobPreference createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public JobPreference updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public JobPreference updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((availableFrom == null) ? 0 : availableFrom.hashCode());
        result = prime * result + ((availableTo == null) ? 0 : availableTo.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
        result = prime * result + ((dailyRate == null) ? 0 : dailyRate.hashCode());
        result = prime * result + ((employmentType == null) ? 0 : employmentType.hashCode());
        result = prime * result + ((engagementType == null) ? 0 : engagementType.hashCode());
        result = prime * result + ((fieldValues == null) ? 0 : fieldValues.hashCode());
        result = prime * result + ((hourPerDay == null) ? 0 : hourPerDay.hashCode());
        result = prime * result + ((hourPerWeek == null) ? 0 : hourPerWeek.hashCode());
        result = prime * result + ((hourlyRate == null) ? 0 : hourlyRate.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((isActive == null) ? 0 : isActive.hashCode());
        result = prime * result + ((locationType == null) ? 0 : locationType.hashCode());
        result = prime * result + ((monthlyRate == null) ? 0 : monthlyRate.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((updatedBy == null) ? 0 : updatedBy.hashCode());
        result = prime * result + ((worker == null) ? 0 : worker.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return (
            "JobPreference [id=" +
            id +
            ", hourlyRate=" +
            hourlyRate +
            ", dailyRate=" +
            dailyRate +
            ", monthlyRate=" +
            monthlyRate +
            ", hourPerDay=" +
            hourPerDay +
            ", hourPerWeek=" +
            hourPerWeek +
            ", engagementType=" +
            engagementType +
            ", employmentType=" +
            employmentType +
            ", locationType=" +
            locationType +
            ", availableFrom=" +
            availableFrom +
            ", availableTo=" +
            availableTo +
            ", isActive=" +
            isActive +
            ", createdBy=" +
            createdBy +
            ", createdAt=" +
            createdAt +
            ", updatedBy=" +
            updatedBy +
            ", updatedAt=" +
            updatedAt +
            ", fieldValues=" +
            fieldValues +
            ", subCategory=" +
            subCategory +
            ", worker=" +
            worker +
            "]"
        );
    }
}
