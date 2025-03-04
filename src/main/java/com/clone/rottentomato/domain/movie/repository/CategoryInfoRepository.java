package com.clone.rottentomato.domain.movie.repository;

import com.clone.rottentomato.domain.movie.component.entity.CategoryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryInfoRepository extends JpaRepository<CategoryInfo, Long> {
    /** 이름을 가진 카테고리 정보 리스트 검색*/
    @Query("SELECT c FROM CategoryInfo c WHERE c.name IN :names")
    List<CategoryInfo> findByNames(List<String> names);

    /** 특정 영화 pk 가 속해있는 카테고리 정보 리스트 반환*/
    @Query("SELECT c FROM MovieCategory m " +
            "INNER JOIN CategoryInfo c ON m.categoryInfo.id = c.id " +
            "WHERE m.movie.id =:movieId")
    List<CategoryInfo> findCategoryInfoForMovieId(Long movieId);
}
