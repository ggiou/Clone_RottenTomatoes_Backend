package com.clone.rottentomato.domain.movie.repository;

import com.clone.rottentomato.domain.movie.component.dto.RecommendMovieDto;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.component.entity.MovieRecommend;
import com.clone.rottentomato.domain.movie.component.entity.id.MovieRecommendId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRecommendRepository extends JpaRepository<MovieRecommend, MovieRecommendId> {
    // 당일날 탐색된 추천 영화가 아니라면 새로 탐색 필요
    // -> 스케줄러나 or 배치로 매일 새로운 추천 영화를 설정하기에는 scope 가 작은 클론 코딩이라 (매일 데이터를 탐색할 필요성 x)
    // 수정 날짜를 기준으로 당일보다 예전이라면, 특정 영화의 추천영화 api 를 호출할 때만 탐색하는 방식으로 채택 했습니다.
    @Query("SELECT new com.clone.rottentomato.domain.movie.component.dto.RecommendMovieDto(r.recommendMovie, r.recommendRank) " +
            " FROM MovieRecommend r INNER JOIN Movie rm ON r.recommendMovie.id = rm.id" +
            " WHERE r.movie.id =:movieId AND r.searchDate >= current date" +
            " ORDER BY r.recommendRank ASC" )
    List<RecommendMovieDto> findValidRecommendMovieByMovie(@Param("movieId") Long movieId);

    List<MovieRecommend> findAllByMovie(Movie movie);

    // 추천 영화 판단은 modDate로 함으로 업데이트 안된애도 변경날짜를 올려줘야한다.
    @Query("UPDATE MovieRecommend r SET r.modDate = NOW() WHERE r =:recommendMovie")
    void updateModDate(@Param("recommendMovie") MovieRecommend recommendMovie);
}
