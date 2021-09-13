package com.simplify.marketplace.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Employment.
 */
@Entity
@Table(name = "employment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class Employment implements Serializable {

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((employeeLocations == null) ? 0 : employeeLocations.hashCode());
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((isCurrent == null) ? 0 : isCurrent.hashCode());
        result = prime * result + ((jobTitle == null) ? 0 : jobTitle.hashCode());
        result = prime * result + ((lastSalary == null) ? 0 : lastSalary.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((updatedBy == null) ? 0 : updatedBy.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return (
            "Employment [id=" +
            id +
            ", jobTitle=" +
            jobTitle +
            ", companyName=" +
            companyName +
            ", startDate=" +
            startDate +
            ", endDate=" +
            endDate +
            ", isCurrent=" +
            isCurrent +
            ", employeeLocations=" +
            employeeLocations +
            ", lastSalary=" +
            lastSalary +
            ", description=" +
            description +
            ", createdBy=" +
            createdBy +
            ", createdAt=" +
            createdAt +
            ", updatedBy=" +
            updatedBy +
            ", updatedAt=" +
            updatedAt +
            ", locations=" +
            locations +
            ", company=" +
            company +
            ", worker=" +
            worker +
            "]"
        );
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Employment other = (Employment) obj;
        if (companyName == null) {
            if (other.companyName != null) return false;
        } else if (!companyName.equals(other.companyName)) return false;
        if (createdAt == null) {
            if (other.createdAt != null) return false;
        } else if (!createdAt.equals(other.createdAt)) return false;
        if (createdBy == null) {
            if (other.createdBy != null) return false;
        } else if (!createdBy.equals(other.createdBy)) return false;
        if (description == null) {
            if (other.description != null) return false;
        } else if (!description.equals(other.description)) return false;
        if (employeeLocations == null) {
            if (other.employeeLocations != null) return false;
        } else if (!employeeLocations.equals(other.employeeLocations)) return false;
        if (endDate == null) {
            if (other.endDate != null) return false;
        } else if (!endDate.equals(other.endDate)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (isCurrent == null) {
            if (other.isCurrent != null) return false;
        } else if (!isCurrent.equals(other.isCurrent)) return false;
        if (jobTitle == null) {
            if (other.jobTitle != null) return false;
        } else if (!jobTitle.equals(other.jobTitle)) return false;
        if (lastSalary == null) {
            if (other.lastSalary != null) return false;
        } else if (!lastSalary.equals(other.lastSalary)) return false;
        if (startDate == null) {
            if (other.startDate != null) return false;
        } else if (!startDate.equals(other.startDate)) return false;
        if (updatedAt == null) {
            if (other.updatedAt != null) return false;
        } else if (!updatedAt.equals(other.updatedAt)) return false;
        if (updatedBy == null) {
            if (other.updatedBy != null) return false;
        } else if (!updatedBy.equals(other.updatedBy)) return false;
        return true;
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "company_name")
    private String companyName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
    @Column(name = "start_date")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_current")
    private Boolean isCurrent;

    @Column(name = "employee_locations")
    private String employeeLocations;

    @Column(name = "last_salary")
    private Integer lastSalary;

    @Column(name = "description")
    private String description;

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

    @OneToMany(mappedBy = "employment", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "locationPrefrences", "employment" }, allowSetters = true)
    private Set<Location> locations = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "customUser" }, allowSetters = true)
    private Client company;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "user", "files", "educations", "certificates", "employments", "portfolios", "refereces", "jobPreferences", "skills" },
        allowSetters = true
    )
    private Worker worker;

    public Employment id(Long id) {
        this.id = id;
        return this;
    }

    public Employment jobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public Employment companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public Employment startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public Employment endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public Employment isCurrent(Boolean isCurrent) {
        this.isCurrent = isCurrent;
        return this;
    }

    public Employment lastSalary(Integer lastSalary) {
        this.lastSalary = lastSalary;
        return this;
    }

    public Employment description(String description) {
        this.description = description;
        return this;
    }

    public Employment locations(Set<Location> locations) {
        this.setLocations(locations);
        return this;
    }

    public Employment addLocation(Location location) {
        this.locations.add(location);
        location.setEmployment(this);
        return this;
    }

    public Employment removeLocation(Location location) {
        this.locations.remove(location);
        location.setEmployment(null);
        return this;
    }

    public Employment company(Client client) {
        this.setCompany(client);
        return this;
    }

    public Employment worker(Worker worker) {
        this.setWorker(worker);
        return this;
    }

    public Employment createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Employment createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Employment updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public Employment updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
