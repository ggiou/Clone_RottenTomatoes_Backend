package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.common.component.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse extends ResponseDto {
    private List<SearchMovieInfo> movieInfos; // 영화 검색 결과 리스트 (영화 제목 or 배우/감독의 이름이 포함될 경우)
    private List<ProducerDto> actors;    // 배우 이름 검색 결과
    private List<ProducerDto> directors; // 감독 이름 검색 결과
    private int movieInfosTotalCnt; // 영화 검색 결과 총 개수
    private int actorsTotalCnt; // 배우 검색 결과 총 개수
    private int directorsTotalCnt;  // 감독 검색 결과 총 개수
    private String searchValue; // 검색 값

    public static SearchResponse of(List<SearchMovieInfo> movieInfos, List<ProducerDto> actors, List<ProducerDto> directors, int movieInfosTotalCnt, int actorsTotalCnt, int directorsTotalCnt, String searchValue){
        return new SearchResponse(movieInfos, actors, directors, movieInfosTotalCnt, actorsTotalCnt, directorsTotalCnt, searchValue);
    }

    public static SearchResponse fromSearchValue(String searchValue){
        return new SearchResponse(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0, 0, 0, searchValue);
    }
}
