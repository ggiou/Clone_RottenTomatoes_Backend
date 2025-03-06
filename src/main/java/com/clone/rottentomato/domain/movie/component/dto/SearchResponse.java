package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.domain.movie.component.entity.MovieProducer;
import com.clone.rottentomato.util.UtilString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    private List<SearchMovieInfo> searchMovieInfos; // 영화 검색 결과 리스트 (영화 제목 or 배우/감독의 이름이 포함될 경우)
    private List<MovieProducer> actors;    // 배우 이름 검색 결과
    private List<MovieProducer> directors; // 감독 이름 검색 결과
    private String searchValue; // 검색 값


    @Getter
    @NoArgsConstructor
    public static class SearchMovieInfo{
        private MovieDto movieDto;  // 영화 정보
        private List<String> actorNames;    // 영화 배우 이름
        private List<String> directorNames; // 영화 감독 이름
        private String actorNamesStr;   // 영화 배우 이름 문자
        private String directorNamesStr;    // 영화 감독 이름 문자

        private SearchMovieInfo(MovieDto movie, List<String> actorNames, List<String> directorNames){
            this.movieDto = movie;
            this.actorNames = actorNames;
            this.directorNames = directorNames;
            this.actorNamesStr = UtilString.joinStrByDelimiter(actorNames, ", ");
            this.directorNamesStr = UtilString.joinStrByDelimiter(directorNames, ", ");
        }

        public SearchMovieInfo of(MovieDto movie, List<String> actorNames, List<String> directorNames){
            return new SearchMovieInfo(movie, actorNames, directorNames);
        }
    }
}
