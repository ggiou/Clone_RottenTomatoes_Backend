package com.clone.rottentomato.domain.movie.repository.custom;

import com.clone.rottentomato.domain.movie.component.dto.MovieDto;
import com.clone.rottentomato.domain.movie.component.dto.RecommendMovieDto;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.component.entity.MovieRecommend;

import java.util.List;

public interface MovieRecommendCustomRepository {
    List<MovieRecommend> saveOrUpdateRecommendMovie(Movie movie, List<Movie> recommendMovie);
}
