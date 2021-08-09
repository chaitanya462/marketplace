package com.simplify.marketplace.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.SkillsMaster} entity.
 */
public class SkillsMasterDTO implements Serializable {

    private Long id;

    private String skillName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SkillsMasterDTO)) {
            return false;
        }

        SkillsMasterDTO skillsMasterDTO = (SkillsMasterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, skillsMasterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SkillsMasterDTO{" +
            "id=" + getId() +
            ", skillName='" + getSkillName() + "'" +
            "}";
    }
}
