package com.clone.rottentomato.domain.movie.repository.custom.impl;

import com.clone.rottentomato.domain.movie.component.entity.MovieProducer;
import com.clone.rottentomato.domain.movie.component.entity.MovieProducerId;
import com.clone.rottentomato.domain.movie.repository.MovieProducerRepository;
import com.clone.rottentomato.domain.movie.repository.custom.MovieProducerCustomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Transactional
@Repository
@RequiredArgsConstructor
public class MovieProducerCustomRepositoryImpl implements MovieProducerCustomRepository {
    private final MovieProducerRepository movieProducerRepository;

    /** 영화 - 제작자 정보 리스트가 없다면 저장, 있으면 업데이트
     * @return 저장한 MovieProducer 리스트 */
    @Override
    public List<MovieProducer> saveMovieProducer(List<MovieProducer> entityList) {
        // db에 영화 - 제작자 연관관계가 저장된게 있는지 확인
        List<MovieProducerId> ids = MovieProducerId.of(entityList);
        List<MovieProducer> movieProducers = movieProducerRepository.findAllById(ids);

        // db에 없는 영화 제작자 정보만 저장
        List<MovieProducer> saveTargetList = entityList.stream().filter(s-> movieProducers.stream()
                .noneMatch(t->t.getMovie().getId().equals(s.getMovie().getId()) && t.getProducer().getId().equals(s.getProducer().getId()))).toList();
        movieProducerRepository.saveAll(saveTargetList);

        // db에 저장된 정보를 list 반환
        movieProducers.addAll(saveTargetList);
        return movieProducers;
    }

}
