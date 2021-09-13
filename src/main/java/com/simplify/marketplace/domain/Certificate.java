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
 * A Certificate.
 */
@Entity
@Table(name = "certificate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class Certificate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "certificate_name")
    private String certificateName;

    @Column(name = "issuer")
    private String issuer;

    @Column(name = "issue_year")
    private Integer issueYear;

    @Column(name = "expiry_year")
    private Integer expiryYear;

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

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "user", "files", "educations", "certificates", "employments", "portfolios", "refereces", "jobPreferences", "skills" },
        allowSetters = true
    )
    private Worker worker;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Certificate id(Long id) {
        this.id = id;
        return this;
    }

    public Certificate certificateName(String certificateName) {
        this.certificateName = certificateName;
        return this;
    }

    public Certificate issuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public Certificate issueYear(Integer issueYear) {
        this.issueYear = issueYear;
        return this;
    }

    public Certificate expiryYear(Integer expiryYear) {
        this.expiryYear = expiryYear;
        return this;
    }

    public Certificate description(String description) {
        this.description = description;
        return this;
    }

    public Certificate worker(Worker worker) {
        this.setWorker(worker);
        return this;
    }

    public Certificate createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Certificate createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Certificate updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public Certificate updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
