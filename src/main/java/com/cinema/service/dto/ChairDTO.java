package com.cinema.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.cinema.domain.Chair} entity.
 */
public class ChairDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 6)
    private String location;

    @NotNull
    private Boolean avaible_chair;

    private BookingDTO booking;

    private MoviefunctionDTO moviefunction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getAvaible_chair() {
        return avaible_chair;
    }

    public void setAvaible_chair(Boolean avaible_chair) {
        this.avaible_chair = avaible_chair;
    }

    public BookingDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }

    public MoviefunctionDTO getMoviefunction() {
        return moviefunction;
    }

    public void setMoviefunction(MoviefunctionDTO moviefunction) {
        this.moviefunction = moviefunction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChairDTO)) {
            return false;
        }

        ChairDTO chairDTO = (ChairDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chairDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChairDTO{" +
            "id=" + getId() +
            ", location='" + getLocation() + "'" +
            ", avaible_chair='" + getAvaible_chair() + "'" +
            ", booking=" + getBooking() +
            ", moviefunction=" + getMoviefunction() +
            "}";
    }
}
