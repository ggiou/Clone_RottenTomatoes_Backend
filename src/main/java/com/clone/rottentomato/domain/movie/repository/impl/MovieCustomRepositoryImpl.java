package com.clone.rottentomato.domain.movie.repository.impl;

import com.clone.rottentomato.domain.movie.component.dto.MovieDetailDto;
import com.clone.rottentomato.domain.movie.component.dto.MovieDto;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.component.entity.MovieDetail;
import com.clone.rottentomato.domain.movie.repository.MovieCustomRepository;
import com.clone.rottentomato.util.UtilJpa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@Repository
public class MovieCustomRepositoryImpl implements MovieCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private UtilJpa<Movie, Long> utilJpaForMovie;
    private UtilJpa<MovieDetail, Long> utilJpaForMovieDetail;

    // ================================= 영화 기본정보 =================================
    /** 영화 기본정보 데이터가 없다면 저장, 있으면 업데이트
     * @return 저장한 movie 객체 단건 */
    @Override
    public Movie saveOrUpdateMovie(Movie entity) {
        return utilJpaForMovie.saveOrUpdateByPk(entity.getId(), entity);
    }

    /** 영화 기본정보 데이터가 없다면 저장, 있으면 업데이트
     * @return 저장한 movieDto 객체 단건 */
    @Override
    public MovieDto returnSaveOrUpdateMovie(Movie entity){
        try {
            return MovieDto.fromEntity(saveOrUpdateMovie(entity), true,"영화 정보 저장에 성공했습니다.");
        }catch (Exception e){
            log.error("[returnSaveOrUpdateMovie] :" + e.getMessage());
            return MovieDto.fromEntity(entity, false, String.format("영화 정보 저장에 실패했습니다.\n[error] %s", e.getMessage()));
        }
    }

    /**  해당 리스트 데이터가 없다면 저장, 있으면 업데이트
     * @return 저장한 movie 객체 리스트*/
    @Override
    @Transactional
    public List<Movie> saveOrUpdateAllMovie(List<Movie> entityList) {
        List<Movie> saveResult = new ArrayList<>();
        for(Movie entity : entityList){
            try {
                saveOrUpdateMovie(entity);
                saveResult.add(entity);
            }catch (Exception e){
                log.error("[saveOrUpdateAllMovie] :" + e.getMessage());
            }
        }
        return saveResult;
    }

    // ================================= 영화 기본정보 =================================
    /** 영화 상세 정보 데이터가 없다면 저장, 있으면 업데이트
     * @return 저장한 movie 객체 단건 */
    @Override
    public MovieDetail saveOrUpdateMovieDetail(MovieDetail entity) {
        return utilJpaForMovieDetail.saveOrUpdateByColumn("movieId", entity.getMovie().getId(), entity);
    }

    /** 영화 상세 정보 데이터가 없다면 저장, 있으면 업데이트
     * @return 저장한 movieDto 객체 단건 */
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
