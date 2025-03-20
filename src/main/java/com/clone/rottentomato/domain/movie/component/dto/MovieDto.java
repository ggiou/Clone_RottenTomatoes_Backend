package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.common.component.dto.ResponseDto;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.util.UtilDate;
import com.clone.rottentomato.util.UtilNumber;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDto extends ResponseDto {
    private Long id;    // 영화 id
    private String name;    // 영화명
    private String rating;  // 영화 평점 (리뷰 별점 기준 -> 0.0 ~ 5.0)
    private String posterUrl;   // 영화 포스터 url
    private String releaseDate;  // 개봉일
    @JsonIgnoreProperties(ignoreUnknown = true)
    private int rank;

    private MovieDto(Long id, String name, String rating, String posterUrl, String releaseDate){
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.posterUrl = posterUrl;
        this.releaseDate = releaseDate;
    }

    public MovieDto(Long id, String name, BigDecimal rating, String posterUrl, LocalDateTime releaseDate){
        this.id = id;
        this.name = name;
        this.rating = String.valueOf(rating);
        this.posterUrl = posterUrl;
        this.releaseDate = UtilDate.toStr(releaseDate);
    }

    //TODO 해당 방식 JPA MovieDto가 생성된다면 위도 동일방식으로 변경
    public MovieDto(Movie movie, int rank){
        this.id = movie.getId();
        this.name = movie.getName();
        this.rating = String.valueOf(movie.getRating());
        this.posterUrl = movie.getPosterUrl();
        this.releaseDate = UtilDate.toStr(movie.getReleaseDate());
        this.rank = rank;
    }

    // 응답값으로 사용시, 성공 실패 여부만 담은 객체 반환
    private MovieDto(boolean success, String resultMsg){
        this.setResult(success, resultMsg);
    }

    // 응답값으로 사용시, 영화 이름, 성공 실패 여부만 담은 객체 반환
    private MovieDto(String movieName, boolean success, String resultMsg){
        this.name = movieName;
        this.setResult(success, resultMsg);
    }

    /** 영화 저장을 위한 객체 생성 */
    public static MovieDto forSave(String name, String posterUrl, String releaseDate){
        return new MovieDto(null, name, null, posterUrl, releaseDate);
    }

    public static MovieDto fromEntity(Movie entity){
        if(Objects.isNull(entity)) return null;
        String releaseDate = UtilDate.toStr(entity.getReleaseDate());
        return new MovieDto(entity.getId(), entity.getName(), String.valueOf(entity.getRating()), entity.getPosterUrl(), releaseDate);
    }

    public static MovieDto fromEntity(Movie dto, boolean isSuccess,String resultMsg){
        if(Objects.isNull(dto)) return new MovieDto(false, resultMsg);
        MovieDto resDto = new MovieDto(dto.getId(), dto.getName(), String.valueOf(dto.getRating()), dto.getPosterUrl(), UtilDate.toStr(dto.getReleaseDate()));
        resDto.setResult(isSuccess, resultMsg);
        return resDto;
    }

    // todo 이거 공통으로 뺄수 있는지 확인, fromResultMsg
    public static MovieDto fromResult(boolean isSuccess, String resultMsg){
        return new MovieDto(isSuccess, resultMsg);
    }

    public static MovieDto fromResult(String movieName, boolean isSuccess, String resultMsg){
        return new MovieDto(movieName, isSuccess, resultMsg);
    }
}
