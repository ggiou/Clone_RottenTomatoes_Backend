package com.clone.rottentomato.domain.movie.repository;


import com.clone.rottentomato.domain.movie.component.entity.MovieDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieDetailRepository extends JpaRepository<MovieDetail, Long> {
    Optional<MovieDetail> findByMovieId(Long movieId);
}
