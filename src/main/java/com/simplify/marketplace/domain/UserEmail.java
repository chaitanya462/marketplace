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
import javax.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A UserEmail.
 */
@Entity
@Table(name = "user_email")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class UserEmail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_primary")
    private Boolean isPrimary;

    @Column(name = "tag")
    private String tag;

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
    private User user;

    public UserEmail id(Long id) {
        this.id = id;
        return this;
    }

    public UserEmail email(String email) {
        this.email = email;
        return this;
    }

    public UserEmail isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public UserEmail isPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
        return this;
    }

    public UserEmail tag(String tag) {
        this.tag = tag;
        return this;
    }

    public UserEmail user(User user) {
        this.setUser(user);
        return this;
    }

    public UserEmail createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public UserEmail createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public UserEmail updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public UserEmail updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
