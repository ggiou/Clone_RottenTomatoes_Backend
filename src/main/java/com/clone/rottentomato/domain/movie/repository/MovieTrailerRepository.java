package com.clone.rottentomato.domain.movie.repository;

import com.clone.rottentomato.domain.movie.component.entity.MovieTrailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MovieTrailerRepository extends JpaRepository<MovieTrailer, Long> {
    Optional<MovieTrailer> findByPlayUrl(String playUrl);

    /** 영화 이름으로, 해당 영화의 트레일러의 마지막 노출 순서 값을 반환
     * @return int (0이라면, 해당 영화의 트레일러가 존재하지 않는 것..)*/
    @Query("SELECT COALESCE(" +
            "(SELECT mt.displayOrder  FROM MovieTrailer mt " +
            "WHERE mt.movie.id = (SELECT m.id FROM Movie m WHERE m.name LIKE %:movieName%) " +
            "ORDER BY mt.displayOrder DESC), 0)")
    int findLastDisplayOrderByMovie(@Param("movieName") String movieName);

}
