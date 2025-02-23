package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.crawling.constant.CrawlingSite;
import com.clone.rottentomato.domain.movie.valid.validation.ValidMovieSaveRequest;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Getter
@ValidMovieSaveRequest
public class MovieSaveRequest {
    private String name;    // 영화 이름
    private int releaseYear;    // 개봉 년도
    private String searchUrl;   // 검색 url
    private CrawlingSite crawlingSite = CrawlingSite.NAMU_WIKI;
}
