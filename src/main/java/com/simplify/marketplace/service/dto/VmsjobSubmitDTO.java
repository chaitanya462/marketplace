package com.simplify.marketplace.service.dto;

import java.io.Serializable;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.VmsjobSubmit} entity.
 */
@Data
public class VmsjobSubmitDTO implements Serializable {

    private Long id;

    private String vmsjobsubmitName;

    private String submissionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVmsjobsubmitName() {
        return vmsjobsubmitName;
    }

    public void setVmsjobsubmitName(String vmsjobsubmitName) {
        this.vmsjobsubmitName = vmsjobsubmitName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VmsjobSubmitDTO)) {
            return false;
        }

        VmsjobSubmitDTO vmsjobSubmitDTO = (VmsjobSubmitDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vmsjobSubmitDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VmsjobSubmitDTO{" +
            "id=" + getId() +
            ", vmsjobsubmitName='" + getVmsjobsubmitName() + "'" +
            "}";
    }
}
