package com.simplify.marketplace.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.simplify.marketplace.domain.enumeration.CompanyType;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_website")
    private String companyWebsite;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_type")
    private CompanyType companyType;

    @Column(name = "primary_phone")
    private String primaryPhone;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "description")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
    @Column(name = "start_date")
    private LocalDate startDate;

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

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    public Client id(Long id) {
        this.id = id;
        return this;
    }

    public Client companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public Client companyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
        return this;
    }

    public Client companyType(CompanyType companyType) {
        this.companyType = companyType;
        return this;
    }

    public Client primaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
        return this;
    }

    public Client isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public Client description(String description) {
        this.description = description;
        return this;
    }

    public Client startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public Client user(User user) {
        this.setUser(user);
        return this;
    }

    public Client createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Client createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Client updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public Client updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
