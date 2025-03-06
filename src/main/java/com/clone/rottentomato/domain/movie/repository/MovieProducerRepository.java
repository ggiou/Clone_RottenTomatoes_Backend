package com.clone.rottentomato.domain.movie.repository;

import com.clone.rottentomato.domain.movie.component.entity.MovieProducer;
import com.clone.rottentomato.domain.movie.component.entity.MovieProducerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieProducerRepository extends JpaRepository<MovieProducer, MovieProducerId> {
    @Query("SELECT mp FROM MovieProducer mp WHERE mp.movie.id IN :ids")
    List<MovieProducer> findAllByMovieIds(@Param("ids") List<Long> ids);

}
