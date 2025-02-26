package com.clone.rottentomato.domain.movie.repository.custom;

import com.clone.rottentomato.domain.movie.component.entity.CategoryInfo;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.component.entity.MovieCategory;

import java.util.List;

public interface MovieCategoryCustomRepository {
    /** 영화 카테고리 정보 저장 */
    void saveMovieCategoryList(List<MovieCategory> entityList);
    
    /** 특정 영화의 카테고리 정보 저장 */
    void saveMovieCategoryList(Movie movie, List<CategoryInfo> categoryInfos);
}
