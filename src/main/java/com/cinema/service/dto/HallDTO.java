package com.cinema.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.cinema.domain.Hall} entity.
 */
public class HallDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 30)
    private Integer cols_hall;

    @NotNull
    @Min(value = 1)
    @Max(value = 28)
    private Integer rows_hall;

    @NotNull
    @Size(min = 1)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCols_hall() {
        return cols_hall;
    }

    public void setCols_hall(Integer cols_hall) {
        this.cols_hall = cols_hall;
    }

    public Integer getRows_hall() {
        return rows_hall;
    }

    public void setRows_hall(Integer rows_hall) {
        this.rows_hall = rows_hall;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HallDTO)) {
            return false;
        }

        HallDTO hallDTO = (HallDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hallDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HallDTO{" +
            "id=" + getId() +
            ", cols_hall=" + getCols_hall() +
            ", rows_hall=" + getRows_hall() +
            ", name='" + getName() + "'" +
            "}";
    }
}
