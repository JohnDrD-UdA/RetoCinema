package com.cinema.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Hall.
 */
@Entity
@Table(name = "hall")
public class Hall implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 30)
    @Column(name = "cols_hall", nullable = false)
    private Integer cols_hall;

    @NotNull
    @Min(value = 1)
    @Max(value = 28)
    @Column(name = "rows_hall", nullable = false)
    private Integer rows_hall;

    @NotNull
    @Size(min = 1)
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Hall id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCols_hall() {
        return this.cols_hall;
    }

    public Hall cols_hall(Integer cols_hall) {
        this.setCols_hall(cols_hall);
        return this;
    }

    public void setCols_hall(Integer cols_hall) {
        this.cols_hall = cols_hall;
    }

    public Integer getRows_hall() {
        return this.rows_hall;
    }

    public Hall rows_hall(Integer rows_hall) {
        this.setRows_hall(rows_hall);
        return this;
    }

    public void setRows_hall(Integer rows_hall) {
        this.rows_hall = rows_hall;
    }

    public String getName() {
        return this.name;
    }

    public Hall name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hall)) {
            return false;
        }
        return id != null && id.equals(((Hall) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Hall{" +
            "id=" + getId() +
            ", cols_hall=" + getCols_hall() +
            ", rows_hall=" + getRows_hall() +
            ", name='" + getName() + "'" +
            "}";
    }
}
