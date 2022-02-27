package com.cinema.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.cinema.domain.Moviefunction} entity.
 */
public class MoviefunctionDTO implements Serializable {

    private Long id;

    @NotNull
    private String movie_date_time;

    @NotNull
    private Boolean active_movie_function;

    private MovieDTO movie;

    private HallDTO hall;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMovie_date_time() {
        return movie_date_time;
    }

    public void setMovie_date_time(String movie_date_time) {
        this.movie_date_time = movie_date_time;
    }

    public Boolean getActive_movie_function() {
        return active_movie_function;
    }

    public void setActive_movie_function(Boolean active_movie_function) {
        this.active_movie_function = active_movie_function;
    }

    public MovieDTO getMovie() {
        return movie;
    }

    public void setMovie(MovieDTO movie) {
        this.movie = movie;
    }

    public HallDTO getHall() {
        return hall;
    }

    public void setHall(HallDTO hall) {
        this.hall = hall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoviefunctionDTO)) {
            return false;
        }

        MoviefunctionDTO moviefunctionDTO = (MoviefunctionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, moviefunctionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoviefunctionDTO{" +
            "id=" + getId() +
            ", movie_date_time='" + getMovie_date_time() + "'" +
            ", active_movie_function='" + getActive_movie_function() + "'" +
            ", movie=" + getMovie() +
            ", hall=" + getHall() +
            "}";
    }
}
