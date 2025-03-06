package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.common.component.dto.SortRequestDto;
import com.clone.rottentomato.common.constant.SortType;
import com.clone.rottentomato.domain.movie.constant.MovieFindType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Objects;

import static com.clone.rottentomato.domain.movie.constant.MovieFindType.ALL;
import static com.clone.rottentomato.common.constant.SortType.*;


/** 특정 영화를 찾기 위한 request */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieFindRequest extends SortRequestDto {
    private String findValue;   // 찾을 값
    private int page = 0;   // 페이지
    private int pageSize = 10;   // 페이지 크기


    // JPQL에서는 엔티티 필드명을 사용해야 함, 엔티티 필드명에 맞게 변환
    public String getSortTypeSql() {
        // 영화 정렬 기본은 등록일 기준으로
        SortType sortType = Objects.isNull(getSortType()) ? SortType.RELEASE_DATE : getSortType();
        return switch (sortType) {
            case RELEASE_DATE -> "releaseDate";
            case ORDER -> "id";
            case NAME -> "name";
            case REGISTER -> "regDate"; 
            case RATING -> "rating";
            default -> "releaseDate";
        };
    }

    public Sort getSort(){
        return isAsc() ? Sort.by(getSortTypeSql()).ascending() : Sort.by(getSortTypeSql()).descending();
    }
}
