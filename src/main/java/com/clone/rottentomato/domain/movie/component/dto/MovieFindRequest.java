package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.common.component.dto.SortRequestDto;
import com.clone.rottentomato.common.constant.SortType;
import com.clone.rottentomato.domain.movie.constant.MovieFindType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.clone.rottentomato.domain.movie.constant.MovieFindType.ALL;
import static com.clone.rottentomato.common.constant.SortType.*;


/** 특정 영화를 찾기 위한 request */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieFindRequest extends SortRequestDto {
    @NotBlank(message = "찾을 값은 필수 입니다.")
    private String findValue;   // 찾을 값
    private int page = 0;   // 페이지
    private int pageSize = 10;   // 페이지 크기


    public String getSortTypeSql() {
        // 영화 정렬 기본은 등록일 기준으로
        SortType sortType = Objects.isNull(getSortType()) ? SortType.RELEASE_DATE : getSortType();
        if (RELEASE_DATE.equals(sortType)) return "releaseDate";
        if (ORDER.equals(sortType)) return "id";
        if (NAME.equals(sortType)) return "name";
        if (REGISTER.equals(sortType)) return "regDate";
        if (RATING.equals(sortType)) return "rating";
        return "releaseDate";
    }
}
