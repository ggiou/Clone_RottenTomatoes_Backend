package com.clone.rottentomato.domain.movie.repository.custom;

import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.component.entity.RecommendMovie;

import java.util.List;

public interface RecommendMovieCustomRepository{
    List<RecommendMovie> saveOrUpdateRecommendMovie(Movie movie, List<Movie> recommendMovie);
}
