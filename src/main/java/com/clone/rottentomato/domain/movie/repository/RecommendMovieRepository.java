package com.clone.rottentomato.domain.movie.repository;

import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.component.entity.RecommendMovie;
import com.clone.rottentomato.domain.movie.component.entity.id.RecommendMovieId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendMovieRepository extends JpaRepository<RecommendMovie, RecommendMovieId> {
    List<Movie> findRecommendMovieByMovie(Movie movie);
}
