package com.clone.rottentomato.domain.movie.repository;


import com.clone.rottentomato.domain.movie.component.entity.MovieDetail;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieDetailRepository extends JpaRepository<MovieDetail, Long> {
    Optional<MovieDetail> findByMovieId(Long movieId);
    @Query(value = "SELECT d FROM movie_detail d " +
            "WHERE d.actor_names like %:name% or d.directo_names like %:name % ", nativeQuery = true)
    List<MovieDetail> findAllByActorOrDirectorNamesContaining(@Param("name") String name);

    /** 입력한 값이 영화 제목 or 배우,감독 이름에 포함되어 있을 경우 반환
     * <pre>
     * [ 정렬 순서 ]
     * 1. 영화 제목 or 배우 or 감독 이름이 정확히 일치하는 경우 (+배우,감독의 경우 일치하는게 존재하는 경우)
     * 2. 영화 제목이 입력 글자에 포함되는 경우 ( 문자% -> %문자%)
     * 3. 감독 배우 이름이 입력 글자에 포함되는 경우
     * </pre>*/
    @Query(value = "SELECT md FROM MovieDetail md JOIN FETCH Movie m ON m.id = md.movie.id " +
            "WHERE REPLACE(m.name, ' ', '') LIKE CONCAT('%', :name, '%') OR REPLACE(md.actorNames, ' ', '') LIKE CONCAT('%', :name, '%') OR REPLACE(md.directorNames, ' ', '') LIKE CONCAT('%', :name, '%') " +
            "ORDER BY CASE WHEN m.name = :name OR FUNCTION('FIND_IN_SET', :name, REPLACE(md.actorNames, ' ', '')) > 0 OR FUNCTION('FIND_IN_SET', :name, REPLACE(md.directorNames, ' ', '')) > 0 THEN 0 " +
            "WHEN m.name LIKE CONCAT(:name, '%') THEN 1 " +
            "WHEN m.name LIKE CONCAT('%', :name, '%') THEN 2 " +
            "WHEN md.actorNames LIKE CONCAT(:name, '%') OR md.directorNames LIKE CONCAT(:name, '%') THEN 3" +
            "WHEN md.actorNames LIKE CONCAT('%', :name, '%') OR md.directorNames LIKE CONCAT('%', :name, '%') THEN 4 " +
            "ELSE 5 END, m.rating DESC, m.releaseDate DESC")
    Page<MovieDetail> searchByNameContaining(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT COUNT(md) FROM MovieDetail md JOIN FETCH Movie m ON m.id = md.movie.id " +
            "WHERE REPLACE(m.name, ' ', '') LIKE CONCAT('%', :name, '%') OR REPLACE(md.actorNames, ' ', '') LIKE CONCAT('%', :name, '%') OR REPLACE(md.directorNames, ' ', '') LIKE CONCAT('%', :name, '%')")
    int countByNameContaining(@Param("name") String name);

}
