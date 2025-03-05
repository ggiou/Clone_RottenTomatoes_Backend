package com.clone.rottentomato.domain.movie.repository.custom;

import com.clone.rottentomato.domain.movie.component.entity.MovieProducer;

import java.util.List;

public interface MovieProducerCustomRepository {
    // 카테고리 정보 저장
    List<MovieProducer> saveMovieProducer(List<MovieProducer> entityList);


}
