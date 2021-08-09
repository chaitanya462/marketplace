package com.simplify.marketplace.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LocationPrefrence.
 */
@Entity
@Table(name = "location_prefrence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LocationPrefrence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prefrence_order")
    private Integer prefrenceOrder;

    @ManyToOne
    @JsonIgnoreProperties(value = { "locationPrefrences", "fieldValues", "subCategory", "worker" }, allowSetters = true)
    private JobPreference worker;

    @ManyToOne
    @JsonIgnoreProperties(value = { "locationPrefrences", "employment" }, allowSetters = true)
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocationPrefrence id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getPrefrenceOrder() {
        return this.prefrenceOrder;
    }

    public LocationPrefrence prefrenceOrder(Integer prefrenceOrder) {
        this.prefrenceOrder = prefrenceOrder;
        return this;
    }

    public void setPrefrenceOrder(Integer prefrenceOrder) {
        this.prefrenceOrder = prefrenceOrder;
    }

    public JobPreference getWorker() {
        return this.worker;
    }

    public LocationPrefrence worker(JobPreference jobPreference) {
        this.setWorker(jobPreference);
        return this;
    }

    public void setWorker(JobPreference jobPreference) {
        this.worker = jobPreference;
    }

    public Location getLocation() {
        return this.location;
    }

    public LocationPrefrence location(Location location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationPrefrence)) {
            return false;
        }
        return id != null && id.equals(((LocationPrefrence) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationPrefrence{" +
            "id=" + getId() +
            ", prefrenceOrder=" + getPrefrenceOrder() +
            "}";
    }
}
