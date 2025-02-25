package com.clone.rottentomato.domain.movie.repository.impl;

import com.clone.rottentomato.domain.movie.component.dto.MovieDetailDto;
import com.clone.rottentomato.domain.movie.component.entity.MovieDetail;
import com.clone.rottentomato.domain.movie.repository.custom.MovieDetailCustomRepository;
import com.clone.rottentomato.util.UtilJpa;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Transactional
@Repository
public class MovieDetailCustomRepositoryImpl implements MovieDetailCustomRepository {
    private UtilJpa<MovieDetail, Long> utilJpaForMovieDetail;

    // ================================= 영화 기본정보 =================================
    /** 영화 상세 정보 데이터가 없다면 저장, 있으면 업데이트
     * @return 저장한 MovieDetail 객체 단건 */
    @Override
    public MovieDetail saveOrUpdateMovieDetail(MovieDetail entity) {
        return utilJpaForMovieDetail.saveOrUpdateByColumn("movieId", entity.getMovie().getId(), entity);
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

}
