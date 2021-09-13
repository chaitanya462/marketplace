package com.simplify.marketplace.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.simplify.marketplace.domain.enumeration.FieldType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.DateFormat;

/**
 * A Field.
 */
@Entity
@Table(name = "field")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class Field implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "field_name")
    private String fieldName;

    @Column(name = "field_label")
    private String fieldLabel;

    @Enumerated(EnumType.STRING)
    @Column(name = "field_type")
    private FieldType fieldType;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_by")
    private String createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(
        type = org.springframework.data.elasticsearch.annotations.FieldType.Date,
        format = DateFormat.date
    )
    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(
        type = org.springframework.data.elasticsearch.annotations.FieldType.Date,
        format = DateFormat.date
    )
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @OneToMany(mappedBy = "field")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "jobpreference", "field" }, allowSetters = true)
    private Set<FieldValue> fieldValues = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "categories", "fields", "parent" }, allowSetters = true)
    private Category category;

    public Field id(Long id) {
        this.id = id;
        return this;
    }

    public Field fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public Field fieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
        return this;
    }

    public Field fieldType(FieldType fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public Field isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public Field fieldValues(Set<FieldValue> fieldValues) {
        this.setFieldValues(fieldValues);
        return this;
    }

    public Field addFieldValue(FieldValue fieldValue) {
        this.fieldValues.add(fieldValue);
        fieldValue.setField(this);
        return this;
    }

    public Field removeFieldValue(FieldValue fieldValue) {
        this.fieldValues.remove(fieldValue);
        fieldValue.setField(null);
        return this;
    }

    public Field category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Field createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Field createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Field updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public Field updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
