package com.clone.rottentomato.domain.movie.repository.custom.impl;

import com.clone.rottentomato.domain.movie.component.entity.Producer;
import com.clone.rottentomato.domain.movie.repository.ProducerRepository;
import com.clone.rottentomato.domain.movie.repository.custom.ProducerCustomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Transactional
@Repository
@RequiredArgsConstructor
public class ProducerCustomRepositoryImpl implements ProducerCustomRepository {
    private final ProducerRepository ProducerRepository;

    /** 카테고리 정보 리스트가 없다면 저장, 있으면 업데이트
     * @return 저장한 CategoryInfo 리스트 */
    @Override
    public List<Producer> saveProducer(List<Producer> entityList) {
        // db에 이름과 동일한 영화 제작자 정보들을 조회
        List<String> names = entityList.stream().map(Producer::getName).toList();
        List<Producer> Producers = ProducerRepository.findByNames(names);

        // db에 없는 영화 제작자 정보만 저장
        List<Producer> saveTargetList = entityList.stream().filter(s-> Producers.stream()
                .noneMatch(t->t.getName().equals(s.getName()) && t.getType().equals(s.getType()))).toList();
        ProducerRepository.saveAll(saveTargetList);

        // db에 저장된 정보를 list 반환
        Producers.addAll(saveTargetList);
        return Producers;
    }

}
