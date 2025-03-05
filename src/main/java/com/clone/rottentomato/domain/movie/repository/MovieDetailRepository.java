package com.clone.rottentomato.domain.movie.repository;


import com.clone.rottentomato.domain.movie.component.entity.MovieDetail;
import jakarta.validation.constraints.Size;
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

}
