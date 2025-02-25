package com.clone.rottentomato.domain.movie.repository.impl;

import com.clone.rottentomato.domain.movie.component.dto.MovieTrailerDto;
import com.clone.rottentomato.domain.movie.component.entity.MovieTrailer;
import com.clone.rottentomato.domain.movie.repository.custom.MovieTrailerCustomRepository;
import com.clone.rottentomato.util.UtilJpa;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Transactional
@Repository
public class MovieTrailerCustomRepositoryImpl implements MovieTrailerCustomRepository {
    private UtilJpa<MovieTrailer, Long> utilJpaForMovieTrailer;

    /** 영화 예고편 데이터가 없다면 저장, 있으면 업데이트
     * @return 저장한 MovieTrailer 객체 단건 */
    @Override
    public MovieTrailer saveOrUpdateMovieTrailer(MovieTrailer entity) {
        return utilJpaForMovieTrailer.saveOrUpdateByColumn("movieId", entity.getMovie().getId(), entity);
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
}
