package com.clone.rottentomato.domain.movie.component.entity;

import java.io.Serializable;
import java.util.Objects;

public class MovieCategoryId implements Serializable{
    private Long movie;
    private Long categoryInfo;

    public MovieCategoryId() {}

    public MovieCategoryId(Long movieId, Long categoryInfoId) {
        this.movie = movieId;
        this.categoryInfo = categoryInfoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieCategoryId that = (MovieCategoryId) o;
        return Objects.equals(movie, that.movie) &&
                Objects.equals(categoryInfo, that.categoryInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie, categoryInfo);
    }
}
