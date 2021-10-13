package com.simplify.marketplace.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.VmsjobSave} entity.
 */
public class VmsjobSaveDTO implements Serializable {

    private Long id;

    private String vmsjobsaveName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVmsjobsaveName() {
        return vmsjobsaveName;
    }

    public void setVmsjobsaveName(String vmsjobsaveName) {
        this.vmsjobsaveName = vmsjobsaveName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VmsjobSaveDTO)) {
            return false;
        }

        VmsjobSaveDTO vmsjobSaveDTO = (VmsjobSaveDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vmsjobSaveDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VmsjobSaveDTO{" +
            "id=" + getId() +
            ", vmsjobsaveName='" + getVmsjobsaveName() + "'" +
            "}";
    }
}
