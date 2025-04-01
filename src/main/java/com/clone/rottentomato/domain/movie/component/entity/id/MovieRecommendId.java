package com.clone.rottentomato.domain.movie.component.entity.id;

import java.io.Serializable;
import java.util.Objects;

public class MovieRecommendId implements Serializable{
    private Long movie;
    private Long recommendMovie;

    public MovieRecommendId() {}

    public MovieRecommendId(Long movieId, Long recommendMovieId) {
        this.movie = movieId;
        this.recommendMovie = recommendMovieId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieRecommendId that = (MovieRecommendId) o;
        return Objects.equals(movie, that.movie) &&
                Objects.equals(recommendMovie, that.recommendMovie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie, recommendMovie);
    }
}
