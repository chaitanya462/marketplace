package com.simplify.marketplace.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.CustomUser} entity.
 */
public class CustomUserDTO implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomUserDTO)) {
            return false;
        }

        CustomUserDTO customUserDTO = (CustomUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomUserDTO{" +
            "id=" + getId() +
            "}";
    }
}
