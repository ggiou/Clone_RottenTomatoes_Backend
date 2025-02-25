package com.clone.rottentomato.domain.movie.repository;


import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.repository.custom.MovieCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long>, MovieCustomRepository {
}
