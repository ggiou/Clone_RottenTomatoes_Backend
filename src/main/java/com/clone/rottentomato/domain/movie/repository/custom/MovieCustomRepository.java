package com.clone.rottentomato.domain.movie.repository.custom;

import com.clone.rottentomato.domain.movie.component.dto.MovieDto;
import com.clone.rottentomato.domain.movie.component.entity.Movie;

import java.util.List;

public interface MovieCustomRepository {
    // 영화 기본 정보 저장
    MovieDto returnSaveOrUpdateMovie(Movie entity);
    List<Movie> saveOrUpdateAllMovie(List<Movie> entityList);
}
