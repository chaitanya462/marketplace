package com.simplify.marketplace.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.Certificate} entity.
 */
@Data
public class CertificateDTO implements Serializable {

    private Long id;

    private String certificateName;

    private String issuer;

    private Integer issueYear;

    private Integer expiryYear;

    private String description;

    private WorkerDTO worker;

    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;
}
