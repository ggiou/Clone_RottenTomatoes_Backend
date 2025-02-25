package com.clone.rottentomato.domain.movie.repository;


import com.clone.rottentomato.domain.movie.component.entity.MovieDetail;
import com.clone.rottentomato.domain.movie.repository.custom.MovieDetailCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieDetailRepository extends JpaRepository<MovieDetail, Long>, MovieDetailCustomRepository {
}
