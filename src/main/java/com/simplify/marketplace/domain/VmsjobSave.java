package com.simplify.marketplace.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.transaction.Transactional;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VmsjobSave.
 */
@Entity
@Table(name = "vmsjob_save")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VmsjobSave implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vmsjobsave_name")
    private String vmsjobsaveName;

    @ManyToMany(mappedBy = "vmsjobsaves",fetch = FetchType.EAGER)
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

    public VmsjobSave id(Long id) {
        this.id = id;
        return this;
    }

    public String getVmsjobsaveName() {
        return this.vmsjobsaveName;
    }

    public VmsjobSave vmsjobsaveName(String vmsjobsaveName) {
        this.vmsjobsaveName = vmsjobsaveName;
        return this;
    }

    public void setVmsjobsaveName(String vmsjobsaveName) {
        this.vmsjobsaveName = vmsjobsaveName;
    }

    @Transactional
    public Set<Worker> getWorkers() {
        return this.workers;
    }

    public VmsjobSave workers(Set<Worker> workers) {
        this.setWorkers(workers);
        return this;
    }

    public VmsjobSave addWorker(Worker worker) {
        this.workers.add(worker);
        worker.getVmsjobsaves().add(this);
        return this;
    }

    public VmsjobSave removeWorker(Worker worker) {
        this.workers.remove(worker);
        worker.getVmsjobsaves().remove(this);
        return this;
    }

    public void setWorkers(Set<Worker> workers) {
        if (this.workers != null) {
            this.workers.forEach(i -> i.removeVmsjobsave(this));
        }
        if (workers != null) {
            workers.forEach(i -> i.addVmsjobsave(this));
        }
        this.workers = workers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VmsjobSave)) {
            return false;
        }
        return id != null && id.equals(((VmsjobSave) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VmsjobSave{" +
            "id=" + getId() +
            ", vmsjobsaveName='" + getVmsjobsaveName() + "'" +
            "}";
    }
}
