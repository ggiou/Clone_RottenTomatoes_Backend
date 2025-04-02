package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.common.component.dto.ResponseDto;
import com.clone.rottentomato.common.valid.validation.ValidDateStr;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.component.entity.MovieDetail;
import com.clone.rottentomato.domain.movie.component.entity.MovieTrailer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

import static com.clone.rottentomato.common.constant.CommonConst.DATE.HOUR_MINUTE;

@Getter
public class MovieTrailerDto extends ResponseDto {
    private Long id;            // 영화 예고편 id
    @Setter
    private int displayOrder;   // 예고편 노출 순서
    private String playUrl;     // 영화 예고편 url
    private String playName;    // 영화 예고편 지정 이름
    @ValidDateStr(dateFormat = HOUR_MINUTE, isNullable = true, fieldName = "예고편 총 재생 시간 (playTime)")
    private String playTime;    // 영화 예고편 총 재생 시간
    private Long movieId;       // 영화 pk

    private MovieTrailerDto(Long id, int displayOrder, String playUrl, String playName, String playTime, Long movieId){
        this.id = id;
        this.displayOrder = displayOrder;
        this.playUrl = playUrl;
        this.playName = playName;
        this.playTime = playTime;
        this.movieId = movieId;
    }

    // 응답값으로 사용시, 성공 실패 여부만 담은 객체 반환
    private MovieTrailerDto(boolean success, String resultMsg){
        this.setResult(success, resultMsg);
    }

    /** 영화 예고편 저장을 위한 객체 */
    public static MovieTrailerDto forSave(int displayOrder, String playUrl, String playName, String playTime){
        return new MovieTrailerDto(null, displayOrder, playUrl, playName, playTime, null);
    }

    public static MovieTrailerDto fromEntity(MovieTrailer entity){
        if(Objects.isNull(entity)) return null;
        return new MovieTrailerDto(entity.getId(), entity.getDisplayOrder(), entity.getPlayUrl(), entity.getPlayName(), entity.getPlayTime(), entity.getMovie().getId());
    }

    public static MovieTrailerDto fromEntity(MovieTrailer entity, boolean isSuccess, String resultMsg){
        if(Objects.isNull(entity)) return new MovieTrailerDto(false, resultMsg);
        MovieTrailerDto resDto = new MovieTrailerDto(entity.getId(), entity.getDisplayOrder(), entity.getPlayUrl(), entity.getPlayName(), entity.getPlayTime(), entity.getMovie().getId());
        resDto.setResult(isSuccess, resultMsg);
        return resDto;
    }


    public static MovieTrailerDto fromResult(boolean isSuccess, String resultMsg){
        return new MovieTrailerDto(isSuccess, resultMsg);
    }
}
