package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.common.component.dto.ResponseDto;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.util.UtilDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDto extends ResponseDto {
    private long id;    // 영화 id
    private String name;    // 영화명
    private BigDecimal rating;  // 영화 평점 (리뷰 별점 기준 -> 0.0 ~ 5.0)
    private String posterUrl;   // 영화 포스터 url
    private String releaseDate;  // 개봉일

    private MovieDto(Long id, String name, BigDecimal rating, String posterUrl, String releaseDate){
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.posterUrl = posterUrl;
        this.releaseDate = releaseDate;
    }


    // 응답값으로 사용시, 성공 실패 여부만 담은 객체 반환
    private MovieDto(boolean success, String resultMsg){
        this.setResult(success, resultMsg);
    }

    public static MovieDto fromEntity(Movie dto){
        if(Objects.isNull(dto)) return null;
        String releaseDate = UtilDate.convertDate(dto.getReleaseDate());
        return new MovieDto(dto.getId(), dto.getName(), dto.getRating(), dto.getPosterUrl(), releaseDate);
    }

    public static MovieDto fromEntity(Movie dto, boolean isSuccess,String resultMsg){
        if(Objects.isNull(dto)) return new MovieDto(false, resultMsg);
        MovieDto resDto = new MovieDto(dto.getId(), dto.getName(), dto.getRating(), dto.getPosterUrl(), UtilDate.convertDate(dto.getReleaseDate()));
        resDto.setResult(isSuccess, resultMsg);
        return resDto;
    }

    // todo 이거 공통으로 뺄수 있는지 확인, fromResultMsg
    public static MovieDto fromResult(boolean isSuccess, String resultMsg){
        return new MovieDto(isSuccess, resultMsg);
    }
}
