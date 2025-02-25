package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.common.component.dto.ResponseDto;
import com.clone.rottentomato.domain.movie.component.entity.MovieCategory;
import lombok.Getter;

import java.util.Objects;

@Getter
public class MovieCategoryInfoDto extends ResponseDto {
    private Long id;    // 영화 카테고리 id
    private Long movieId;   // 영화 pk
    private Long categoryId;    // 카테고리 pk
    private String categoryName;    // 영화 카테고리 이름

    private MovieCategoryInfoDto(Long id, Long movieId, Long categoryId, String categoryName) {
        this.id = id;
        this.movieId = movieId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // 응답값으로 사용시, 성공 실패 여부만 담은 객체 반환
    private MovieCategoryInfoDto(boolean success, String resultMsg){
        this.setResult(success, resultMsg);
    }

    public static MovieCategoryInfoDto fromEntity(MovieCategory entity){
        if(Objects.isNull(entity)) return null;
        return new MovieCategoryInfoDto(entity.getId(), entity.getMovie().getId(), entity.getCategoryInfo().getId(), entity.getCategoryInfo().getName());
    }

    public static MovieCategoryInfoDto fromEntity(MovieCategory entity, boolean isSuccess, String resultMsg){
        if(Objects.isNull(entity)) return new MovieCategoryInfoDto(false, resultMsg);
        MovieCategoryInfoDto resDto = new MovieCategoryInfoDto(entity.getId(), entity.getMovie().getId(), entity.getCategoryInfo().getId(), entity.getCategoryInfo().getName());
        resDto.setResult(isSuccess, resultMsg);
        return resDto;
    }

    // todo 이거 공통으로 뺄수 있는지 확인, fromResultMsg
    public static MovieCategoryInfoDto fromResult(boolean isSuccess, String resultMsg){
        return new MovieCategoryInfoDto(isSuccess, resultMsg);
    }

}
