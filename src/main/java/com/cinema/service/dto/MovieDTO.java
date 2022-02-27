package com.cinema.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.cinema.domain.Movie} entity.
 */
public class MovieDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String gendre;

    @NotNull
    @Size(max = 250)
    private String synopsis;

    @NotNull
    @Size(max = 10)
    private String movie_format;

    @NotNull
    @Size(max = 20)
    private String movie_length;

    @Lob
    private byte[] poster;

    private String posterContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGendre() {
        return gendre;
    }

    public void setGendre(String gendre) {
        this.gendre = gendre;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getMovie_format() {
        return movie_format;
    }

    public void setMovie_format(String movie_format) {
        this.movie_format = movie_format;
    }

    public String getMovie_length() {
        return movie_length;
    }

    public void setMovie_length(String movie_length) {
        this.movie_length = movie_length;
    }

    public byte[] getPoster() {
        return poster;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
    }

    public String getPosterContentType() {
        return posterContentType;
    }

    public void setPosterContentType(String posterContentType) {
        this.posterContentType = posterContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MovieDTO)) {
            return false;
        }

        MovieDTO movieDTO = (MovieDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, movieDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MovieDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", gendre='" + getGendre() + "'" +
            ", synopsis='" + getSynopsis() + "'" +
            ", movie_format='" + getMovie_format() + "'" +
            ", movie_length='" + getMovie_length() + "'" +
            ", poster='" + getPoster() + "'" +
            "}";
    }
}
