package com.clone.rottentomato.domain.movie.repository.custom.impl;

import com.clone.rottentomato.common.constant.JpaError;
import com.clone.rottentomato.domain.movie.component.entity.Producer;
import com.clone.rottentomato.domain.movie.repository.ProducerRepository;
import com.clone.rottentomato.domain.movie.repository.custom.ProducerCustomRepository;
import com.clone.rottentomato.exception.JpaException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@Repository
@RequiredArgsConstructor
public class ProducerCustomRepositoryImpl implements ProducerCustomRepository {
    private final ProducerRepository producerRepository;

    /** 제작자 정보 리스트가 없다면 저장, 있으면 업데이트
     * @return 저장한 Producer 리스트 */
    @Override
    public List<Producer> saveProducer(List<Producer> entityList) {
        // db에 이름과 동일한 영화 제작자 정보들을 조회
        List<String> names = entityList.stream().map(Producer::getName).toList();
        if(names.isEmpty()) throw new JpaException("제작자 정보를 저장하기엔 잘못된 입력입니다.", JpaError.SAVE_ERROR);
        List<Producer> producers = producerRepository.findByNames(names);

        // db에 없는 영화 제작자 정보만 저장
        if(producers.isEmpty()) return producerRepository.saveAll(entityList);
        List<Producer> saveTargetList = entityList.stream().filter(s-> producers.stream()
                .noneMatch(t->t.getName().equals(s.getName()) && t.getRoleType().equals(s.getRoleType()))).toList();
        producerRepository.saveAll(saveTargetList);

        // db에 저장된 정보를 list 반환
        producers.addAll(saveTargetList);
        return producers;
    }

}
