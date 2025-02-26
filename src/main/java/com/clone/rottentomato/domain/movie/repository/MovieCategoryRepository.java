package com.clone.rottentomato.domain.movie.repository;

import com.clone.rottentomato.domain.movie.component.entity.MovieCategory;
import com.clone.rottentomato.domain.movie.repository.custom.MovieCategoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieCategoryRepository extends JpaRepository<MovieCategory, Long>, MovieCategoryCustomRepository {
    /** 영화 ID 중에 해당 카테고리 ID를 가지고 있는 영화 카테고리 연결 정보를 찾아 반환
     * <br> = select * from MOVIE_CATEGORY where MOVIE_ID = ? and CATEGORY_INFO_ID IN (?)*/
    List<MovieCategory> findByMovieIdAndCategoryInfoIdIn(Long movieId, List<Long> categoryInfoIds);
}
