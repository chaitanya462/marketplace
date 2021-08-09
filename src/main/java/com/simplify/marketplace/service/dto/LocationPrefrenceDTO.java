package com.simplify.marketplace.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.LocationPrefrence} entity.
 */
public class LocationPrefrenceDTO implements Serializable {

    private Long id;

    private Integer prefrenceOrder;

    private JobPreferenceDTO worker;

    private LocationDTO location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPrefrenceOrder() {
        return prefrenceOrder;
    }

    public void setPrefrenceOrder(Integer prefrenceOrder) {
        this.prefrenceOrder = prefrenceOrder;
    }

    public JobPreferenceDTO getWorker() {
        return worker;
    }

    public void setWorker(JobPreferenceDTO worker) {
        this.worker = worker;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationPrefrenceDTO)) {
            return false;
        }

        LocationPrefrenceDTO locationPrefrenceDTO = (LocationPrefrenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, locationPrefrenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationPrefrenceDTO{" +
            "id=" + getId() +
            ", prefrenceOrder=" + getPrefrenceOrder() +
            ", worker=" + getWorker() +
            ", location=" + getLocation() +
            "}";
    }
}
