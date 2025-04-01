package com.clone.rottentomato.domain.movie.repository.custom.impl;

import com.clone.rottentomato.domain.movie.component.dto.MovieDto;
import com.clone.rottentomato.domain.movie.component.dto.RecommendMovieDto;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.component.entity.MovieRecommend;
import com.clone.rottentomato.domain.movie.repository.MovieRecommendRepository;
import com.clone.rottentomato.domain.movie.repository.custom.MovieRecommendCustomRepository;
import com.clone.rottentomato.util.UtilJpa;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class MovieRecommendCustomRepositoryImpl implements MovieRecommendCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private final MovieRecommendRepository movieRecommendRepository;
    private final UtilJpa<MovieRecommend> utilJpa;
    private final ObjectMapper mapper = new ObjectMapper();


    // ================================= 영화 기본정보 =================================
    /** 기존 특정 영화의 추천 영화 리스트에 따라 저장 혹은 업데이트 - 일자로 판단하기 때문
     * @return 저장한 전체 추천영화 리스트 */
    @Override
    @Transactional
    public List<MovieRecommend> saveOrUpdateRecommendMovie(Movie movie, List<Movie> recommendMovie) {
        if(Objects.isNull(movie) || CollectionUtils.isEmpty(recommendMovie)) return null;
        // 특정 영화의 추천 영화 전부 업데이트
        List<MovieRecommend> originRecommends = movieRecommendRepository.findAllByMovie(movie);
        int originRecommendsSize = originRecommends.size();
        int recommendMovieSize = recommendMovie.size();
        // 추천 영화들이 없다면 저장
        if(originRecommendsSize == 0) {
            AtomicInteger recommendRank = new AtomicInteger(1);
            // 10개 이상이면 10개까지만 자르기 (추천 영화는 10개까지만 저장하기로 설정)
            if(recommendMovieSize > 10) recommendMovie = recommendMovie.subList(0, 9);
            // 영화 id가 존재하는 애들만 추천 리스트 가능
            List<MovieRecommend> saveList = recommendMovie.stream()
                    .filter(t-> !Objects.isNull(t.getId()))
                    .map(t-> MovieRecommend.of(movie, t, recommendRank.getAndIncrement())).toList();
            return movieRecommendRepository.saveAll(saveList);
        }else{
            // 있다면 값만 업데이트
            List<MovieRecommend> returnRecommendMovies = new ArrayList<>();
            // 추천 영화를 10개 까지 고정
            for(int i=0; i<10; i++){
                MovieRecommend origin = originRecommends.get(i);
                if(!Objects.isNull(origin)) {
                    Movie newRecommend = recommendMovieSize > i ? recommendMovie.get(i) : origin.getRecommendMovie();
                    origin.changeRecommendMovie(newRecommend);
                }else{
                    Movie newRecommend = recommendMovie.get(i);
                    if(Objects.isNull(newRecommend)) break;
                    origin = movieRecommendRepository.save(MovieRecommend.of(movie, newRecommend, i+1));
                }
                returnRecommendMovies.add(origin);
            }

            return returnRecommendMovies;
        }
    }

}
