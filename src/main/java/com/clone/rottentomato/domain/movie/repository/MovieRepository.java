package com.clone.rottentomato.domain.movie.repository;


import com.clone.rottentomato.domain.movie.component.dto.MovieDto;
import com.clone.rottentomato.domain.movie.component.dto.RecommendMovieDto;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByName(String movieName);
    @Query("SELECT new com.clone.rottentomato.domain.movie.component.dto.MovieDto(m.id, m.name, m.rating, m.posterUrl, m.releaseDate) FROM Movie m")
    Page<MovieDto> findAllMovies(Pageable pageable);
    @Query("SELECT COUNT(m) FROM Movie m")
    int countMovie();
    @Query("SELECT COUNT(m) FROM Movie m INNER JOIN MovieCategory c ON m.id=c.movie.id WHERE c.categoryInfo.id =:categoryId")
    int countMovieByCategory(@Param("categoryId") Long categoryId);

    // 해당 영화를 저장한 회원들이 저장한 다른 영화들, 여러번 저장된 순으로 size 개 가져오기 (pageable default 10)
    @Query("SELECT new com.clone.rottentomato.domain.movie.component.dto.RecommendMovieDto(s.movie, COUNT(s.movie.id))" +
            " FROM Saved s" +
            " WHERE s.movie.id != :#{#movie.id}" +
            " AND s.member.memberId  IN (SELECT s2.member.memberId FROM Saved s2 WHERE s2.movie.id = :#{#movie.id})" +
            " GROUP BY s.movie.id" +
            " ORDER BY COUNT(s.movie) DESC")
    List<RecommendMovieDto> findSavedMoviesByMembersWhoSavedThis(@Param("movie") Movie movie, Pageable pageable);

    // 해당 영화를 좋아요한 회원들이 좋아요한 다른 영화들, 여러번 저장된 순으로 size 개 가져오기 (pageable default 10)
    @Query("SELECT new com.clone.rottentomato.domain.movie.component.dto.RecommendMovieDto(s.movie, COUNT(s.movie.id))" +
            " FROM Likes s" +
            " WHERE s.movie.id != :#{#movie.id}" +
            " AND s.member.memberId  IN (SELECT s2.member.memberId FROM Likes s2 WHERE s2.movie.id = :#{#movie.id})" +
            " GROUP BY s.movie.id" +
            " ORDER BY COUNT(s.movie) DESC")
    List<RecommendMovieDto> findLikedMoviesByMembersWhoLikedThis(@Param("movie") Movie movie, Pageable pageable);

    // 해당 영화를 별점 준 사람들이, 해당 영화 별점보다 더 높거나 같은 점수를 준 영화들을 대상으로 평점차를 더해 높은 순으로 size 개 가져오기  (pageable default 10)
    @Query("SELECT new com.clone.rottentomato.domain.movie.component.dto.RecommendMovieDto(r.movie, SUM(r.rating - :#{#movie.rating} + 1))" +
            " FROM Review r " +
            " WHERE r.movie.id != :#{#movie.id}" +
            " AND r.member IN (SELECT r2.member  FROM Review r2 WHERE r2.movie.id = :#{#movie.id})" +
            " AND r.rating >= :#{#movie.rating}" +
            " GROUP BY r.movie.id" +
            " ORDER BY SUM(r.rating - :#{#movie.rating} + 1) DESC")
    List<RecommendMovieDto> findReviewedMoviesByMembersWhoReviewThis(@Param("movie") Movie movie, Pageable pageable);

    // 해당 영화와 동일한 장르를 여러개 가지면서, (동일 장르 개수 * 영화 평점) 높은 순으로 size개 가져오기 (+ 제외할 영화일 경우 미포함 = 이미 추천된 영화 )
    // (점수 : 카테고리 개수 * 영화 평점 -> 영화 장르의 개수 한계와, 추천시 장르에 대한 점수 비중을 높이기 위해 이처럼 설정)
    @Query("SELECT new com.clone.rottentomato.domain.movie.component.dto.RecommendMovieDto(mc.movie, CAST((COUNT(mc.categoryInfo.id) * mc.movie.rating) AS long))" +
            " FROM MovieCategory mc" +
            " WHERE mc.movie.id != :#{#movie.id}" +
            " AND mc.movie.id NOT IN :#{#excludesMovieIds == null or #excludesMovieIds.isEmpty() ? T(java.util.List).of(-1) : #excludesMovieIds}"+
            " AND mc.categoryInfo.id IN (SELECT mc2.categoryInfo.id  FROM MovieCategory mc2 WHERE mc2.movie.id = :#{#movie.id} )" +
            " GROUP BY mc.movie.id" +
            " ORDER BY  (COUNT(mc.categoryInfo.id) * mc.movie.rating) DESC")
    List<RecommendMovieDto> findMoviesByMovieCategoryInclude(@Param("movie") Movie movie, @Param("excludesMovieIds") List<Long> excludesMovieIds , Pageable pageable);

}
