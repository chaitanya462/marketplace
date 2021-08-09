package com.simplify.marketplace.service.dto;

import com.simplify.marketplace.domain.enumeration.PortfolioType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.Portfolio} entity.
 */
public class PortfolioDTO implements Serializable {

    private Long id;

    private String portfolioURL;

    private PortfolioType type;

    private WorkerDTO worker;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPortfolioURL() {
        return portfolioURL;
    }

    public void setPortfolioURL(String portfolioURL) {
        this.portfolioURL = portfolioURL;
    }

    public PortfolioType getType() {
        return type;
    }

    public void setType(PortfolioType type) {
        this.type = type;
    }

    public WorkerDTO getWorker() {
        return worker;
    }

    public void setWorker(WorkerDTO worker) {
        this.worker = worker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PortfolioDTO)) {
            return false;
        }

        PortfolioDTO portfolioDTO = (PortfolioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, portfolioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PortfolioDTO{" +
            "id=" + getId() +
            ", portfolioURL='" + getPortfolioURL() + "'" +
            ", type='" + getType() + "'" +
            ", worker=" + getWorker() +
            "}";
    }
}
