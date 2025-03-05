package com.clone.rottentomato.domain.movie.repository;

import com.clone.rottentomato.domain.movie.component.entity.MovieProducer;
import com.clone.rottentomato.domain.movie.component.entity.MovieProducerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieProducerRepository extends JpaRepository<MovieProducer, MovieProducerId> {
    List<MovieProducer> findAllByMovieProducerId(List<MovieProducerId> ids);
    //todo 아 진심 왜 안ㄴ되냐고!!!!! 수정하기
}
