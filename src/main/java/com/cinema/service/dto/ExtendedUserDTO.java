package com.cinema.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.cinema.domain.ExtendedUser} entity.
 */
public class ExtendedUserDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 6)
    private String identification;

    @NotNull
    private String identification_type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getIdentification_type() {
        return identification_type;
    }

    public void setIdentification_type(String identification_type) {
        this.identification_type = identification_type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExtendedUserDTO)) {
            return false;
        }

        ExtendedUserDTO extendedUserDTO = (ExtendedUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, extendedUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtendedUserDTO{" +
            "id=" + getId() +
            ", identification='" + getIdentification() + "'" +
            ", identification_type='" + getIdentification_type() + "'" +
            "}";
    }
}
