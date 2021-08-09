package com.simplify.marketplace.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.FieldValue} entity.
 */
public class FieldValueDTO implements Serializable {

    private Long id;

    private String value;

    private JobPreferenceDTO jobpreference;

    private FieldDTO field;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public JobPreferenceDTO getJobpreference() {
        return jobpreference;
    }

    public void setJobpreference(JobPreferenceDTO jobpreference) {
        this.jobpreference = jobpreference;
    }

    public FieldDTO getField() {
        return field;
    }

    public void setField(FieldDTO field) {
        this.field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FieldValueDTO)) {
            return false;
        }

        FieldValueDTO fieldValueDTO = (FieldValueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fieldValueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FieldValueDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", jobpreference=" + getJobpreference() +
            ", field=" + getField() +
            "}";
    }
}
