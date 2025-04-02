package com.clone.rottentomato.domain.movie.repository.custom;

import com.clone.rottentomato.domain.movie.component.dto.MovieDetailDto;
import com.clone.rottentomato.domain.movie.component.entity.MovieDetail;

public interface MovieDetailCustomRepository {
    // 영화 상세 정보 저장
    MovieDetail saveOrUpdateMovieDetail(MovieDetail entity);
    MovieDetailDto returnSaveOrUpdateMovieDetail(MovieDetail entity);
}
