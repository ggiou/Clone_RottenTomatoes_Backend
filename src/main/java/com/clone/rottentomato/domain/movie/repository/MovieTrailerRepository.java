package com.clone.rottentomato.domain.movie.repository;

import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.component.entity.MovieTrailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MovieTrailerRepository extends JpaRepository<MovieTrailer, Long> {
    Optional<MovieTrailer> findByPlayUrl(String playUrl);
    @Query(value = "select nullif(mt.displayOrder + 1, 1) from MovieTrailer mt " +
            "where mt.movie = :movie order by mt.displayOrder desc limit 1", nativeQuery = true)
    int findNewDisPlayOrderByMovie(Movie movie);
}
