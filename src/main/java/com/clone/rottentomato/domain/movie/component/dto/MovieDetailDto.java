package com.clone.rottentomato.domain.movie.component.dto;

import com.amazonaws.util.CollectionUtils;
import com.clone.rottentomato.common.component.dto.ResponseDto;
import com.clone.rottentomato.domain.movie.component.entity.MovieDetail;
import com.clone.rottentomato.util.UtilString;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

@Getter
public class MovieDetailDto extends ResponseDto {
    private Long id;    // 영화 상세정보 id
    private String story;   // 영화 줄거리
    private String actorNames;  // 영화 배우 이름들
    private String directorNames;   // 영화 감독 이름들

    private List<String> actorNameList;    // 영화 배우 이름 list
    private List<String> directorNameList; // 영화 감독 이름 list

    private Long movieId;  // 영화 pk

    private MovieDetailDto(Long id, String story, String actorNames, String directorNames ,List<String> actorNamesList, List<String> directorNameList, Long movieId){
        this.id = id;
        this.story = story;
        this.actorNames = actorNames;
        this.directorNames = directorNames;
        this.movieId = movieId;
        this.actorNameList = actorNamesList;
        this.directorNameList = directorNameList;
    }

    // 응답값으로 사용시, 성공 실패 여부만 담은 객체 반환
    private MovieDetailDto(boolean success, String resultMsg){
        this.setResult(success, resultMsg);
    }

    public static MovieDetailDto forSave(String story, String actorNames, String directorNames, List<String> actorNameList, List<String> directorNameList){
        return new MovieDetailDto(null, story, actorNames, directorNames, actorNameList, directorNameList, null);
    }

    public static MovieDetailDto fromEntity(MovieDetail entity){
        if(Objects.isNull(entity)) return null;
        List<String> actorNameList = UtilString.makeListByDelimiter(entity.getActorNames(), ",");
        List<String> directNameList = UtilString.makeListByDelimiter(entity.getActorNames(), ",");
        return new MovieDetailDto(entity.getId(), entity.getStory(), entity.getActorNames(), entity.getDirectorNames(), actorNameList, directNameList, entity.getMovie().getId());
    }

    public static MovieDetailDto fromEntity(MovieDetail entity, boolean isSuccess, String resultMsg){
        if(Objects.isNull(entity)) return new MovieDetailDto(false, resultMsg);
        MovieDetailDto resDto = MovieDetailDto.fromEntity(entity);
        if(Objects.isNull(resDto)) return new MovieDetailDto(false, resultMsg);
        resDto.setResult(isSuccess, resultMsg);
        return resDto;
    }

    // todo 이거 공통으로 뺄수 있는지 확인, fromResultMsg
    public static MovieDetailDto fromResult(boolean isSuccess, String resultMsg){
        return new MovieDetailDto(isSuccess, resultMsg);
    }
}
