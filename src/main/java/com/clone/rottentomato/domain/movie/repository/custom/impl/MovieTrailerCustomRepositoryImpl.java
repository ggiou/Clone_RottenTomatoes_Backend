package com.clone.rottentomato.domain.movie.repository.custom.impl;

import com.clone.rottentomato.domain.movie.component.dto.MovieTrailerDto;
import com.clone.rottentomato.domain.movie.component.entity.MovieDetail;
import com.clone.rottentomato.domain.movie.component.entity.MovieTrailer;
import com.clone.rottentomato.domain.movie.repository.MovieTrailerRepository;
import com.clone.rottentomato.domain.movie.repository.custom.MovieTrailerCustomRepository;
import com.clone.rottentomato.util.UtilJpa;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Transactional
@Repository
@RequiredArgsConstructor
public class MovieTrailerCustomRepositoryImpl implements MovieTrailerCustomRepository {
    private final MovieTrailerRepository movieTrailerRepository;
    private final UtilJpa<MovieTrailer> utilJpa;

    /** 영화 예고편 데이터가 없다면 저장, 있으면 업데이트
     * @return 저장한 MovieTrailer 객체 단건 */
    @Override
    public MovieTrailer saveOrUpdateMovieTrailer(MovieTrailer entity) {
        if(Objects.isNull(entity)) return null;
        if(!Objects.isNull(entity.getId())) {
            // id 있다면, 영화 상세 정보 pk로 찾아 없다면 save, 있다면 다른 값만 update.
            return saveOrUpdate(movieTrailerRepository.findById(entity.getId()), entity);
        }
        if(!Objects.isNull(entity.getMovie()) && !Objects.isNull(entity.getMovie().getId())) {
            if (!Objects.isNull(entity.getPlayUrl()) && entity.getDisplayOrder() > 0){
                Optional<MovieTrailer> movieTrailerOptional = movieTrailerRepository.findByPlayUrl(entity.getPlayUrl());
                return saveOrUpdate(movieTrailerOptional, entity);
            }
        }
        // 영화 상세정보는, 영화가 존재해야지만, 저장이 가능하다.
        throw new NullPointerException("저장할 수 없는 데이터 입니다.");
    }

    /** 영화 예고편 데이터가 없다면 저장, 있으면 업데이트
     * @return 저장한 MovieTrailerDto 객체 단건 */
    @Override
    public MovieTrailerDto returnSaveOrUpdateMovieTrailer(MovieTrailer entity) {
        try {
            return MovieTrailerDto.fromEntity(saveOrUpdateMovieTrailer(entity), true,"영화 예고편 정보 저장에 성공했습니다.");
        }catch (Exception e){
            log.error("[returnSaveOrUpdateMovieTrailer] :" + e.getMessage());
            return MovieTrailerDto.fromEntity(entity, false, String.format("영화 예고편 정보 저장에 실패했습니다..\n[error] %s", e.getMessage()));
        }
    }

    private MovieTrailer saveOrUpdate(Optional<MovieTrailer> findDbEntity, MovieTrailer requestEntity){
        // 이미 존재한다면, null 이 아닌 값만 업데이트
        return findDbEntity.map(movieTrailer -> movieTrailerRepository.save(utilJpa.setNotEqualsProperties(movieTrailer, requestEntity))).orElseGet(() -> movieTrailerRepository.save(requestEntity));
    }
}
