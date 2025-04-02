package com.clone.rottentomato.domain.movie.repository;

import com.clone.rottentomato.domain.movie.component.dto.RecommendMovieDto;
import com.clone.rottentomato.domain.movie.component.entity.MovieCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieCategoryRepository extends JpaRepository<MovieCategory, Long> {
    /** 영화 ID 중에 해당 카테고리 ID를 가지고 있는 영화 카테고리 연결 정보를 찾아 반환
     * <br> = select * from MOVIE_CATEGORY where MOVIE_ID = ? and CATEGORY_INFO_ID IN (?)*/
    List<MovieCategory> findByMovieIdAndCategoryInfoIdIn(Long movieId, List<Long> categoryInfoIds);

    /** 특정 영화와 카테고리가 몇개 동일한지 가져오기 (곱할거라 +1) (카테고리 점수 판단을 위해 매칭되는 개수 가져오기) */
    @Query("SELECT new com.clone.rottentomato.domain.movie.component.dto.RecommendMovieDto(rm.movie, (COUNT(tm.categoryInfo.id) + 1))" +
            " FROM MovieCategory rm" +
            " LEFT JOIN MovieCategory tm ON tm.movie.id =:targetMovieId AND tm.categoryInfo.id = rm.categoryInfo.id" +
            " WHERE rm.movie.id IN :recommendMovieIds" +
            " GROUP BY rm.movie.id")
    List<RecommendMovieDto> findMovieCategoryMatches(@Param("targetMovieId") Long targetMovieId, @Param("recommendMovieIds") List<Long> recommendMovieIds);
}
