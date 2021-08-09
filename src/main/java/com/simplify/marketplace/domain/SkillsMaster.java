package com.simplify.marketplace.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SkillsMaster.
 */
@Entity
@Table(name = "skills_master")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SkillsMaster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "skill_name")
    private String skillName;

    @ManyToMany(mappedBy = "skills")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "customUser", "files", "educations", "certificates", "employments", "portfolios", "refereces", "jobPreferences", "skills",
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

    public SkillsMaster id(Long id) {
        this.id = id;
        return this;
    }

    public String getSkillName() {
        return this.skillName;
    }

    public SkillsMaster skillName(String skillName) {
        this.skillName = skillName;
        return this;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Set<Worker> getWorkers() {
        return this.workers;
    }

    public SkillsMaster workers(Set<Worker> workers) {
        this.setWorkers(workers);
        return this;
    }

    public SkillsMaster addWorker(Worker worker) {
        this.workers.add(worker);
        worker.getSkills().add(this);
        return this;
    }

    public SkillsMaster removeWorker(Worker worker) {
        this.workers.remove(worker);
        worker.getSkills().remove(this);
        return this;
    }

    public void setWorkers(Set<Worker> workers) {
        if (this.workers != null) {
            this.workers.forEach(i -> i.removeSkill(this));
        }
        if (workers != null) {
            workers.forEach(i -> i.addSkill(this));
        }
        this.workers = workers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SkillsMaster)) {
            return false;
        }
        return id != null && id.equals(((SkillsMaster) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SkillsMaster{" +
            "id=" + getId() +
            ", skillName='" + getSkillName() + "'" +
            "}";
    }
}
