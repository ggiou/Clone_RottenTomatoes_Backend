package com.clone.rottentomato.domain.movie.repository.custom.impl;

import com.clone.rottentomato.domain.movie.component.entity.CategoryInfo;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.component.entity.MovieCategory;
import com.clone.rottentomato.domain.movie.repository.MovieCategoryRepository;
import com.clone.rottentomato.domain.movie.repository.custom.MovieCategoryCustomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Repository
@RequiredArgsConstructor
public class MovieCategoryCustomRepositoryImpl implements MovieCategoryCustomRepository {
    private final MovieCategoryRepository movieCategoryRepository;

    /**
     * 영화-카테고리 매핑 정보가 존재하지 않을 경우만 저장
     */
    @Override
    public void saveMovieCategoryList(List<MovieCategory> entityList) {
        // 영화 별로 카테고리 리스트 분리
        Map<Long, List<MovieCategory>> movieMap = entityList.stream()
                .collect(Collectors.groupingBy(t->t.getMovie().getId()));

        // 영화 카테고리가 존재하는지 검색 해 존재하지 않은 값 구분
        List<MovieCategory> saveRequireList = new ArrayList<>();
        for (Long movieId : movieMap.keySet()){
            List<MovieCategory> movieCategories = movieMap.get(movieId);
            // 이미 저장되어있는 정보
            List<MovieCategory> savedList = movieCategoryRepository.findByMovieIdAndCategoryInfoIdIn(movieId, movieCategories.stream().map(t->t.getCategoryInfo().getId()).toList());
            // 이미 저장되어있는 정보가 아닌 경우
            saveRequireList = movieCategories.stream().filter(t->savedList.stream().noneMatch(saved-> Objects.equals(saved.getCategoryInfo().getId(), t.getCategoryInfo().getId()))).toList();
        }

        // 존재하지 않는 값도 모두 저장해 저장되있던 애들과, 저장된 애들 모두 응답 값으로 반환
        movieCategoryRepository.saveAll(saveRequireList);
    }

    /**
     * 영화-카테고리 매핑 정보가 존재하지 않을 경우만 저장
     * */
    @Override
    public void saveMovieCategoryList(Movie movie, List<CategoryInfo> categoryInfos) {
        // 이미 저장되어있는 정보
        List<MovieCategory> savedList = movieCategoryRepository.findByMovieIdAndCategoryInfoIdIn(movie.getId(), categoryInfos.stream().map(CategoryInfo::getId).toList());
        // 특정 영화에 대해 저장되어 있지 않은, 카테고리 정보들
        List<MovieCategory> saveRequireList = categoryInfos.stream()
                .filter(t->savedList.stream().noneMatch(saved-> Objects.equals(saved.getCategoryInfo().getId(), t.getId())))
                .map(t->MovieCategory.of(movie, t))
                .toList();
        movieCategoryRepository.saveAll(saveRequireList);
    }
}
