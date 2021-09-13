package com.simplify.marketplace.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A LocationPrefrence.
 */
@Entity
@Table(name = "location_prefrence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class LocationPrefrence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prefrence_order")
    private Integer prefrenceOrder;

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

    @ManyToOne
    @JsonIgnoreProperties(value = { "locationPrefrences", "fieldValues", "subCategory", "worker" }, allowSetters = true)
    private JobPreference worker;

    @ManyToOne
    @JsonIgnoreProperties(value = { "locationPrefrences", "employment" }, allowSetters = true)
    private Location location;

    public LocationPrefrence id(Long id) {
        this.id = id;
        return this;
    }

    public LocationPrefrence prefrenceOrder(Integer prefrenceOrder) {
        this.prefrenceOrder = prefrenceOrder;
        return this;
    }

    public LocationPrefrence worker(JobPreference jobPreference) {
        this.setWorker(jobPreference);
        return this;
    }

    public LocationPrefrence location(Location location) {
        this.setLocation(location);
        return this;
    }

    public LocationPrefrence createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocationPrefrence createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocationPrefrence updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public LocationPrefrence updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((prefrenceOrder == null) ? 0 : prefrenceOrder.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((updatedBy == null) ? 0 : updatedBy.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return (
            "LocationPrefrence [id=" +
            id +
            ", prefrenceOrder=" +
            prefrenceOrder +
            ", createdBy=" +
            createdBy +
            ", createdAt=" +
            createdAt +
            ", updatedBy=" +
            updatedBy +
            ", updatedAt=" +
            updatedAt +
            "]"
        );
    }
}
