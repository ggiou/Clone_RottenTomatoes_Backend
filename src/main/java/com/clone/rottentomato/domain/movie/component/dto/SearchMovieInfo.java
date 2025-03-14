package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.domain.movie.component.entity.Movie;
import lombok.*;


@Data
@NoArgsConstructor
@Builder
public class SearchMovieInfo {
    private int index;
    private MovieDto movieDto;  // 영화 정보
    private String actorNames;    // 영화 배우
    private String directorNames; // 영화 감독


    private SearchMovieInfo(int index, MovieDto movie, String actorNames, String directorNames) {
        this.index = index;
        this.movieDto = movie;  // MovieDto 인스턴스 생성
        this.actorNames = actorNames;
        this.directorNames = directorNames;
    }

    public static SearchMovieInfo of(int index, Movie movie, String actorNames, String directorNames){
        return new SearchMovieInfo(index, MovieDto.fromEntity(movie), actorNames, directorNames);
    }

}
