package com.clone.rottentomato.domain.movie.repository;

import com.clone.rottentomato.domain.movie.component.dto.MovieDetailDto;
import com.clone.rottentomato.domain.movie.component.dto.MovieDto;
import com.clone.rottentomato.domain.movie.component.dto.MovieTrailerDto;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.component.entity.MovieDetail;
import com.clone.rottentomato.domain.movie.component.entity.MovieTrailer;

import java.util.List;

public interface MovieCustomRepository {
    // 영화 기본 정보 저장
    Movie saveOrUpdateMovie(Movie entity);
    MovieDto returnSaveOrUpdateMovie(Movie entity);
    List<Movie> saveOrUpdateAllMovie(List<Movie> entityList);

    // 영화 상세 정보 저장
    MovieDetail saveOrUpdateMovieDetail(MovieDetail entity);
    MovieDetailDto returnSaveOrUpdateMovieDetail(MovieDetail entity);

    // 영화 예고편 정보 저장
    MovieTrailer saveOrUpdateMovieTrailer(MovieTrailer entity);
    MovieTrailerDto returnSaveOrUpdateMovieTrailer(MovieTrailer entity);

}
