package com.cinema.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Chair.
 */
@Entity
@Table(name = "chair")
public class Chair implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "location", length = 6, nullable = false)
    private String location;

    @NotNull
    @Column(name = "avaible_chair", nullable = false)
    private Boolean avaible_chair;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Booking booking;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "movie", "hall" }, allowSetters = true)
    private Moviefunction moviefunction;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Chair id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return this.location;
    }

    public Chair location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getAvaible_chair() {
        return this.avaible_chair;
    }

    public Chair avaible_chair(Boolean avaible_chair) {
        this.setAvaible_chair(avaible_chair);
        return this;
    }

    public void setAvaible_chair(Boolean avaible_chair) {
        this.avaible_chair = avaible_chair;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Chair booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    public Moviefunction getMoviefunction() {
        return this.moviefunction;
    }

    public void setMoviefunction(Moviefunction moviefunction) {
        this.moviefunction = moviefunction;
    }

    public Chair moviefunction(Moviefunction moviefunction) {
        this.setMoviefunction(moviefunction);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Chair)) {
            return false;
        }
        return id != null && id.equals(((Chair) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Chair{" +
            "id=" + getId() +
            ", location='" + getLocation() + "'" +
            ", avaible_chair='" + getAvaible_chair() + "'" +
            "}";
    }
}
