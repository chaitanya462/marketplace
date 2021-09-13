package com.simplify.marketplace.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.simplify.marketplace.domain.enumeration.PortfolioType;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Portfolio.
 */
@Entity
@Table(name = "portfolio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class Portfolio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "portfolio_url")
    private String portfolioURL;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PortfolioType type;

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

    public Portfolio id(Long id) {
        this.id = id;
        return this;
    }

    public Portfolio portfolioURL(String portfolioURL) {
        this.portfolioURL = portfolioURL;
        return this;
    }

    public Portfolio type(PortfolioType type) {
        this.type = type;
        return this;
    }

    public Portfolio worker(Worker worker) {
        this.setWorker(worker);
        return this;
    }

    public Portfolio createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Portfolio createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Portfolio updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public Portfolio updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
