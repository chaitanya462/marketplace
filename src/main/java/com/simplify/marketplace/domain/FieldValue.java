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
 * A FieldValue.
 */
@Entity
@Table(name = "field_value")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class FieldValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private String value;

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
    private JobPreference jobpreference;

    @ManyToOne
    @JsonIgnoreProperties(value = { "fieldValues", "category" }, allowSetters = true)
    private Field field;

    public FieldValue id(Long id) {
        this.id = id;
        return this;
    }

    public FieldValue value(String value) {
        this.value = value;
        return this;
    }

    public FieldValue jobpreference(JobPreference jobPreference) {
        this.setJobpreference(jobPreference);
        return this;
    }

    public FieldValue field(Field field) {
        this.setField(field);
        return this;
    }

    public FieldValue createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public FieldValue createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public FieldValue updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public FieldValue updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
