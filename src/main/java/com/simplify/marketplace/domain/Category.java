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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_parent")
    private Boolean isParent;

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

    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "categories", "fields", "parent" }, allowSetters = true)
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "category")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "fieldValues", "category" }, allowSetters = true)
    private Set<Field> fields = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "categories", "fields", "parent" }, allowSetters = true)
    private Category parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Category id(Long id) {
        this.id = id;
        return this;
    }

    public Category name(String name) {
        this.name = name;
        return this;
    }

    public Category isParent(Boolean isParent) {
        this.isParent = isParent;
        return this;
    }

    public Category isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public Category categories(Set<Category> categories) {
        this.setCategories(categories);
        return this;
    }

    public Category addCategory(Category category) {
        this.categories.add(category);
        category.setParent(this);
        return this;
    }

    public Category removeCategory(Category category) {
        this.categories.remove(category);
        category.setParent(null);
        return this;
    }

    public Category fields(Set<Field> fields) {
        this.setFields(fields);
        return this;
    }

    public Category addField(Field field) {
        this.fields.add(field);
        field.setCategory(this);
        return this;
    }

    public Category removeField(Field field) {
        this.fields.remove(field);
        field.setCategory(null);
        return this;
    }

    public Category parent(Category category) {
        this.setParent(category);
        return this;
    }

    public Category createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Category createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Category updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public Category updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
        result = prime * result + ((fields == null) ? 0 : fields.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((isActive == null) ? 0 : isActive.hashCode());
        result = prime * result + ((isParent == null) ? 0 : isParent.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((updatedBy == null) ? 0 : updatedBy.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return (
            "Category [id=" +
            id +
            ", name=" +
            name +
            ", isParent=" +
            isParent +
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
            ", fields=" +
            fields +
            ", parent=" +
            parent +
            "]"
        );
    }
}
