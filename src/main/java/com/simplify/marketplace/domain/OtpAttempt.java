package com.simplify.marketplace.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.simplify.marketplace.domain.enumeration.OtpStatus;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A OtpAttempt.
 */
@Entity
@Table(name = "otp_attempt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class OtpAttempt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "context_id")
    private String contextId;

    @Column(name = "otp")
    private Integer otp;

    @Column(name = "is_active")
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OtpStatus status;

    @Column(name = "ip")
    private String ip;

    @Column(name = "coookie")
    private String coookie;

    @Column(name = "created_by")
    private String createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
    @Column(name = "created_at")
    private LocalDate createdAt;

    public OtpAttempt id(Long id) {
        this.id = id;
        return this;
    }

    public OtpAttempt contextId(String contextId) {
        this.contextId = contextId;
        return this;
    }

    public OtpAttempt otp(Integer otp) {
        this.otp = otp;
        return this;
    }

    public OtpAttempt isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public OtpAttempt status(OtpStatus status) {
        this.status = status;
        return this;
    }

    public OtpAttempt ip(String ip) {
        this.ip = ip;
        return this;
    }

    public OtpAttempt coookie(String coookie) {
        this.coookie = coookie;
        return this;
    }

    public OtpAttempt createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public OtpAttempt createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
