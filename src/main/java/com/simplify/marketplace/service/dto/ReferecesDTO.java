package com.simplify.marketplace.service.dto;

import com.simplify.marketplace.domain.enumeration.RelationType;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.Refereces} entity.
 */
public class ReferecesDTO implements Serializable {

    private Long id;

    private String name;

    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    private String email;

    @Min(value = 1000000000)
    @Max(value = 9999999999L)
    private Integer phone;

    private String profileLink;

    private RelationType relationType;

    private WorkerDTO worker;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getProfileLink() {
        return profileLink;
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
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
        if (!(o instanceof ReferecesDTO)) {
            return false;
        }

        ReferecesDTO referecesDTO = (ReferecesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, referecesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReferecesDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone=" + getPhone() +
            ", profileLink='" + getProfileLink() + "'" +
            ", relationType='" + getRelationType() + "'" +
            ", worker=" + getWorker() +
            "}";
    }
}
