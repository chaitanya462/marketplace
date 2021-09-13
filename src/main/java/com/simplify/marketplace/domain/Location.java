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
import javax.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class Location implements Serializable {

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((pincode == null) ? 0 : pincode.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((updatedBy == null) ? 0 : updatedBy.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Location other = (Location) obj;
        if (city == null) {
            if (other.city != null) return false;
        } else if (!city.equals(other.city)) return false;
        if (country == null) {
            if (other.country != null) return false;
        } else if (!country.equals(other.country)) return false;
        if (createdAt == null) {
            if (other.createdAt != null) return false;
        } else if (!createdAt.equals(other.createdAt)) return false;
        if (createdBy == null) {
            if (other.createdBy != null) return false;
        } else if (!createdBy.equals(other.createdBy)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (pincode == null) {
            if (other.pincode != null) return false;
        } else if (!pincode.equals(other.pincode)) return false;
        if (state == null) {
            if (other.state != null) return false;
        } else if (!state.equals(other.state)) return false;
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

    @Min(value = 10000)
    @Max(value = 99999)
    @Column(name = "pincode")
    private Integer pincode;

    @Column(name = "country")
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

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

    @OneToMany(mappedBy = "location")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "worker", "location" }, allowSetters = true)
    private Set<LocationPrefrence> locationPrefrences = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "locations", "company", "worker" }, allowSetters = true)
    private Employment employment;

    public Location id(Long id) {
        this.id = id;
        return this;
    }

    public Location pincode(Integer pincode) {
        this.pincode = pincode;
        return this;
    }

    public Location country(String country) {
        this.country = country;
        return this;
    }

    public Location state(String state) {
        this.state = state;
        return this;
    }

    public Location city(String city) {
        this.city = city;
        return this;
    }

    public Location locationPrefrences(Set<LocationPrefrence> locationPrefrences) {
        this.setLocationPrefrences(locationPrefrences);
        return this;
    }

    public Location addLocationPrefrence(LocationPrefrence locationPrefrence) {
        this.locationPrefrences.add(locationPrefrence);
        locationPrefrence.setLocation(this);
        return this;
    }

    public Location removeLocationPrefrence(LocationPrefrence locationPrefrence) {
        this.locationPrefrences.remove(locationPrefrence);
        locationPrefrence.setLocation(null);
        return this;
    }

    public Location employment(Employment employment) {
        this.setEmployment(employment);
        return this;
    }

    public Location createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Location createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Location updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public Location updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @Override
    public String toString() {
        return (
            "Location [id=" +
            id +
            ", pincode=" +
            pincode +
            ", country=" +
            country +
            ", state=" +
            state +
            ", city=" +
            city +
            ", createdBy=" +
            createdBy +
            ", createdAt=" +
            createdAt +
            ", updatedBy=" +
            updatedBy +
            ", updatedAt=" +
            updatedAt +
            ", locationPrefrences=" +
            locationPrefrences +
            ", =" +
            "]"
        );
    }
}
