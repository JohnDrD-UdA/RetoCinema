package com.cinema.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Moviefunction.
 */
@Entity
@Table(name = "moviefunction")
public class Moviefunction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "movie_date_time", nullable = false)
    private String movie_date_time;

    @NotNull
    @Column(name = "active_movie_function", nullable = false)
    private Boolean active_movie_function;

    @ManyToOne(optional = false)
    @NotNull
    private Movie movie;

    @ManyToOne(optional = false)
    @NotNull
    private Hall hall;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Moviefunction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMovie_date_time() {
        return this.movie_date_time;
    }

    public Moviefunction movie_date_time(String movie_date_time) {
        this.setMovie_date_time(movie_date_time);
        return this;
    }

    public void setMovie_date_time(String movie_date_time) {
        this.movie_date_time = movie_date_time;
    }

    public Boolean getActive_movie_function() {
        return this.active_movie_function;
    }

    public Moviefunction active_movie_function(Boolean active_movie_function) {
        this.setActive_movie_function(active_movie_function);
        return this;
    }

    public void setActive_movie_function(Boolean active_movie_function) {
        this.active_movie_function = active_movie_function;
    }

    public Movie getMovie() {
        return this.movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Moviefunction movie(Movie movie) {
        this.setMovie(movie);
        return this;
    }

    public Hall getHall() {
        return this.hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public Moviefunction hall(Hall hall) {
        this.setHall(hall);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Moviefunction)) {
            return false;
        }
        return id != null && id.equals(((Moviefunction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Moviefunction{" +
            "id=" + getId() +
            ", movie_date_time='" + getMovie_date_time() + "'" +
            ", active_movie_function='" + getActive_movie_function() + "'" +
            "}";
    }
}
