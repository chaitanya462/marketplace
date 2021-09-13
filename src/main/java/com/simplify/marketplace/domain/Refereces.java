package com.simplify.marketplace.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.simplify.marketplace.domain.enumeration.RelationType;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Refereces.
 */
@Entity
@Table(name = "refereces")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class Refereces implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "profile_link")
    private String profileLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation_type")
    private RelationType relationType;

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
    @JsonIgnoreProperties(
        value = { "user", "files", "educations", "certificates", "employments", "portfolios", "refereces", "jobPreferences", "skills" },
        allowSetters = true
    )
    private Worker worker;

    public Refereces id(Long id) {
        this.id = id;
        return this;
    }

    public Refereces name(String name) {
        this.name = name;
        return this;
    }

    public Refereces email(String email) {
        this.email = email;
        return this;
    }

    public Refereces phone(String phone) {
        this.phone = phone;
        return this;
    }

    public Refereces profileLink(String profileLink) {
        this.profileLink = profileLink;
        return this;
    }

    public Refereces relationType(RelationType relationType) {
        this.relationType = relationType;
        return this;
    }

    public Refereces worker(Worker worker) {
        this.setWorker(worker);
        return this;
    }

    public Refereces createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Refereces createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Refereces updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public Refereces updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
