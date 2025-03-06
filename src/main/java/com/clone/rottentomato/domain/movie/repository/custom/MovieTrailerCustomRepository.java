package com.clone.rottentomato.domain.movie.repository.custom;

import com.clone.rottentomato.domain.movie.component.dto.MovieTrailerDto;
import com.clone.rottentomato.domain.movie.component.entity.MovieTrailer;

public interface MovieTrailerCustomRepository {
    // 영화 예고편 정보 저장
    MovieTrailer saveOrUpdateMovieTrailer(MovieTrailer entity);
    MovieTrailerDto returnSaveOrUpdateMovieTrailer(MovieTrailer entity);
}
