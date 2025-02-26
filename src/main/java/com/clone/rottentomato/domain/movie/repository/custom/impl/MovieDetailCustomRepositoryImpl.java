package com.clone.rottentomato.domain.movie.repository.custom.impl;

import com.clone.rottentomato.domain.movie.component.dto.MovieDetailDto;
import com.clone.rottentomato.domain.movie.component.entity.MovieDetail;
import com.clone.rottentomato.domain.movie.repository.MovieDetailRepository;
import com.clone.rottentomato.domain.movie.repository.custom.MovieDetailCustomRepository;
import com.clone.rottentomato.util.UtilJpa;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Transactional
@Repository
@RequiredArgsConstructor
public class MovieDetailCustomRepositoryImpl implements MovieDetailCustomRepository {
    private final MovieDetailRepository movieDetailRepository;
    private final UtilJpa<MovieDetail> utilJpa;

    // ================================= 영화 기본정보 =================================
    /** 영화 상세 정보 데이터가 없다면 저장, 있으면 업데이트
     * @return 저장한 MovieDetail 객체 단건 */
    @Override
    public MovieDetail saveOrUpdateMovieDetail(MovieDetail entity) {
        if(Objects.isNull(entity)) return null;
        if(!Objects.isNull(entity.getId())) {
            // id 있다면, 영화 상세 정보 pk로 찾아 없다면 save, 있다면 다른 값만 update.
            return saveOrUpdate(movieDetailRepository.findById(entity.getId()), entity);
        }
        if(!Objects.isNull(entity.getMovie()) && !Objects.isNull(entity.getMovie().getId())) {
            // 외래 키인, 영화 pk 가 존재한다면, 무저건 1:1므로, 해당 값으로 찾아 있다면 save, 없다면 다른 값만 update
            return saveOrUpdate(movieDetailRepository.findByMovieId(entity.getMovie().getId()), entity);
        }
        // 영화 상세정보는, 영화가 존재해야지만, 저장이 가능하다.
        throw new NullPointerException("저장할 수 없는 데이터 입니다.");
    }

    /** 영화 상세 정보 데이터가 없다면 저장, 있으면 업데이트
     * @return 저장한 MovieDetailDto 객체 단건 */
    @Override
    public MovieDetailDto returnSaveOrUpdateMovieDetail(MovieDetail entity){
        try {
            return MovieDetailDto.fromEntity(saveOrUpdateMovieDetail(entity), true,"영화 상세 정보 저장에 성공했습니다.");
        }catch (Exception e){
            log.error("[returnSaveOrUpdateMovieDetail] :" + e.getMessage());
            return MovieDetailDto.fromEntity(entity, false,String.format("영화 상세 정보 저장에 실패했습니다..\n[error] %s", e.getMessage()));
        }
    }

    private MovieDetail saveOrUpdate(Optional<MovieDetail> findDbEntity, MovieDetail requestEntity){
        // 이미 존재한다면, null 이 아닌 값만 업데이트
        if(findDbEntity.isPresent()) {
            utilJpa.setNotEqualsProperties(findDbEntity.get(), requestEntity);
            return movieDetailRepository.save(findDbEntity.get());
        }
        return movieDetailRepository.save(requestEntity);
    }

}
