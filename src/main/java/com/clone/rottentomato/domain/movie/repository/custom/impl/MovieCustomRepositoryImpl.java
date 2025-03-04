package com.clone.rottentomato.domain.movie.repository.custom.impl;

import com.clone.rottentomato.domain.movie.component.dto.MovieDto;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.repository.MovieRepository;
import com.clone.rottentomato.domain.movie.repository.custom.MovieCustomRepository;
import com.clone.rottentomato.util.UtilJpa;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class MovieCustomRepositoryImpl implements MovieCustomRepository {
    private final MovieRepository movieRepository;
    private final UtilJpa<Movie> utilJpa;


    // ================================= 영화 기본정보 =================================
    /** 영화 기본정보 데이터가 없다면 저장, 있으면 업데이트
     * @return 저장한 movie 객체 단건 */
    @Override
    public Movie saveOrUpdateMovie(Movie entity) {
        if(Objects.isNull(entity)) return null;
        if(!Objects.isNull(entity.getId())) {
            // id 있다면, 영화 정보 pk로 찾아 없다면 save, 있다면 다른 값만 update.
            return saveOrUpdate(movieRepository.findById(entity.getId()), entity);
        }
        if(!StringUtils.isBlank(entity.getName())) {
            // id 값이 없다면, 영화 정보 이름으로 찾아 없다면 save, 있다면 다른 값만 update.
            return saveOrUpdate(movieRepository.findByName(entity.getName()), entity);
        }
        // 영화의 경우, 구분자를 pk or name 으로 해, 없다면 저장이 불가능하게 한다.
        throw new NullPointerException("저장할 수 없는 데이터 입니다.");
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

    private Movie saveOrUpdate(Optional<Movie> findDbEntity, Movie requestEntity){
        // 이미 존재한다면, null 이 아닌 값만 업데이트
        // 영화 평점의 경우, 리뷰 점수를 통해 쌓이므로, 업데이트 되선 안된다. (모든 값이 동일해도 호출 x)
        return findDbEntity.map(movie -> movieRepository.save(utilJpa.setNotEqualsProperties(movie, requestEntity, Collections.singletonList("rating")))).orElseGet(() -> movieRepository.save(requestEntity));
    }

}
