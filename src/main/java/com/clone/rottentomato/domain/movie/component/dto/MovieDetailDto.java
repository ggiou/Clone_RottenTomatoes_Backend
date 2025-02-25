package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.common.component.dto.ResponseDto;
import com.clone.rottentomato.domain.movie.component.entity.MovieDetail;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class MovieDetailDto extends ResponseDto {
    private Long id;    // 영화 상세정보 id
    private String story;   // 영화 줄거리
    private List<String> actorNames;  // 영화 배우 이름들
    private List<String> directorNames;   // 영화 감독 이름들

    private long movieId;  // 영화 pk

    private MovieDetailDto(Long id, String story, List<String> actorNames, List<String> directorNames, long movieId){
        this.id = id;
        this.story = story;
        this.actorNames = actorNames;
        this.directorNames = directorNames;
        this.movieId = movieId;
    }

    // 응답값으로 사용시, 성공 실패 여부만 담은 객체 반환
    private MovieDetailDto(boolean success, String resultMsg){
        this.setResult(success, resultMsg);
    }

    public static MovieDetailDto fromEntity(MovieDetail entity){
        if(Objects.isNull(entity)) return null;
        return new MovieDetailDto(entity.getId(), entity.getStory(), entity.getActorNames(), entity.getDirectorNames(), entity.getMovie().getId());
    }

    public static MovieDetailDto fromEntity(MovieDetail entity, boolean isSuccess, String resultMsg){
        if(Objects.isNull(entity)) return new MovieDetailDto(false, resultMsg);
        MovieDetailDto resDto = new MovieDetailDto(entity.getId(), entity.getStory(), entity.getActorNames(), entity.getDirectorNames(), entity.getMovie().getId());
        resDto.setResult(isSuccess, resultMsg);
        return resDto;
    }

    // todo 이거 공통으로 뺄수 있는지 확인, fromResultMsg
    public static MovieDetailDto fromResult(boolean isSuccess, String resultMsg){
        return new MovieDetailDto(isSuccess, resultMsg);
    }
}
