package com.clone.rottentomato.domain.movie.repository.custom;

import com.clone.rottentomato.domain.movie.component.dto.MovieDto;
import com.clone.rottentomato.domain.movie.component.dto.MovieFindRequest;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface MovieCustomRepository {
    // 영화 기본 정보 저장
    MovieDto returnSaveOrUpdateMovie(Movie entity);
    List<Movie> saveOrUpdateAllMovie(List<Movie> entityList);
    Movie saveOrUpdateMovie(Movie entity);

    /** 카테고리 pk or name이 장르에 속하는 영화를 찾아 카테고리 이름, 영화 dto로 반환 */
    List<MovieDto> findPageByCategory(Long categoryId, Pageable pageable);
    List<Movie> findPage(Pageable pageable);
}
