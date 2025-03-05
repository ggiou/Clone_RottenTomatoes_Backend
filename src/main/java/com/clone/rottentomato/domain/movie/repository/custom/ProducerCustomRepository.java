package com.clone.rottentomato.domain.movie.repository.custom;

import com.clone.rottentomato.domain.movie.component.entity.Producer;

import java.util.List;

public interface ProducerCustomRepository {
    // 카테고리 정보 저장
    List<Producer> saveProducer(List<Producer> entityList);


}
