package com.simplify.marketplace.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VmsjobSubmit.
 */
@Entity
@Table(name = "vmsjob_submit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VmsjobSubmit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vmsjobsubmit_name")
    private String vmsjobsubmitName;

    @ManyToMany(mappedBy = "vmsjobsubmits")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "user",
            "files",
            "educations",
            "certificates",
            "employments",
            "portfolios",
            "refereces",
            "jobPreferences",
            "skills",
            "vmsjobsaves",
            "vmsjobsubmits",
        },
        allowSetters = true
    )
    private Set<Worker> workers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VmsjobSubmit id(Long id) {
        this.id = id;
        return this;
    }

    public String getVmsjobsubmitName() {
        return this.vmsjobsubmitName;
    }

    public VmsjobSubmit vmsjobsubmitName(String vmsjobsubmitName) {
        this.vmsjobsubmitName = vmsjobsubmitName;
        return this;
    }

    public void setVmsjobsubmitName(String vmsjobsubmitName) {
        this.vmsjobsubmitName = vmsjobsubmitName;
    }

    public Set<Worker> getWorkers() {
        return this.workers;
    }

    public VmsjobSubmit workers(Set<Worker> workers) {
        this.setWorkers(workers);
        return this;
    }

    public VmsjobSubmit addWorker(Worker worker) {
        this.workers.add(worker);
        worker.getVmsjobsubmits().add(this);
        return this;
    }

    public VmsjobSubmit removeWorker(Worker worker) {
        this.workers.remove(worker);
        worker.getVmsjobsubmits().remove(this);
        return this;
    }

    public void setWorkers(Set<Worker> workers) {
        if (this.workers != null) {
            this.workers.forEach(i -> i.removeVmsjobsubmit(this));
        }
        if (workers != null) {
            workers.forEach(i -> i.addVmsjobsubmit(this));
        }
        this.workers = workers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VmsjobSubmit)) {
            return false;
        }
        return id != null && id.equals(((VmsjobSubmit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VmsjobSubmit{" +
            "id=" + getId() +
            ", vmsjobsubmitName='" + getVmsjobsubmitName() + "'" +
            "}";
    }
}
