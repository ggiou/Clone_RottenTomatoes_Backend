package com.clone.rottentomato.domain.movie.repository;

import com.clone.rottentomato.domain.movie.component.entity.MovieTrailer;
import com.clone.rottentomato.domain.movie.repository.custom.MovieTrailerCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieTrailerRepository extends JpaRepository<MovieTrailer, Long>, MovieTrailerCustomRepository {

}
