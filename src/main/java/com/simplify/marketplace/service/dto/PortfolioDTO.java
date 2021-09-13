package com.simplify.marketplace.service.dto;

import com.simplify.marketplace.domain.enumeration.PortfolioType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.Portfolio} entity.
 */
@Data
public class PortfolioDTO implements Serializable {

    private Long id;

    private String portfolioURL;

    private PortfolioType type;

    private WorkerDTO worker;

    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;
}
