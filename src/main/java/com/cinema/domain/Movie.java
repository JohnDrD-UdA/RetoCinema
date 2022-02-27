package com.cinema.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Movie.
 */
@Entity
@Table(name = "movie")
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "gendre", nullable = false)
    private String gendre;

    @NotNull
    @Size(max = 250)
    @Column(name = "synopsis", length = 250, nullable = false)
    private String synopsis;

    @NotNull
    @Size(max = 10)
    @Column(name = "movie_format", length = 10, nullable = false)
    private String movie_format;

    @NotNull
    @Size(max = 20)
    @Column(name = "movie_length", length = 20, nullable = false)
    private String movie_length;

    @Lob
    @Column(name = "poster", nullable = false)
    private byte[] poster;

    @NotNull
    @Column(name = "poster_content_type", nullable = false)
    private String posterContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Movie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Movie name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGendre() {
        return this.gendre;
    }

    public Movie gendre(String gendre) {
        this.setGendre(gendre);
        return this;
    }

    public void setGendre(String gendre) {
        this.gendre = gendre;
    }

    public String getSynopsis() {
        return this.synopsis;
    }

    public Movie synopsis(String synopsis) {
        this.setSynopsis(synopsis);
        return this;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getMovie_format() {
        return this.movie_format;
    }

    public Movie movie_format(String movie_format) {
        this.setMovie_format(movie_format);
        return this;
    }

    public void setMovie_format(String movie_format) {
        this.movie_format = movie_format;
    }

    public String getMovie_length() {
        return this.movie_length;
    }

    public Movie movie_length(String movie_length) {
        this.setMovie_length(movie_length);
        return this;
    }

    public void setMovie_length(String movie_length) {
        this.movie_length = movie_length;
    }

    public byte[] getPoster() {
        return this.poster;
    }

    public Movie poster(byte[] poster) {
        this.setPoster(poster);
        return this;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
    }

    public String getPosterContentType() {
        return this.posterContentType;
    }

    public Movie posterContentType(String posterContentType) {
        this.posterContentType = posterContentType;
        return this;
    }

    public void setPosterContentType(String posterContentType) {
        this.posterContentType = posterContentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Movie)) {
            return false;
        }
        return id != null && id.equals(((Movie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Movie{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", gendre='" + getGendre() + "'" +
            ", synopsis='" + getSynopsis() + "'" +
            ", movie_format='" + getMovie_format() + "'" +
            ", movie_length='" + getMovie_length() + "'" +
            ", poster='" + getPoster() + "'" +
            ", posterContentType='" + getPosterContentType() + "'" +
            "}";
    }
}
