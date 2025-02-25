package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.common.valid.validation.ValidEnumType;
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
    @ValidEnumType(enumClass = CrawlingSite.class, fieldName = "크롤링 대상 사이트_crawlingSite")
    private CrawlingSite crawlingSite = CrawlingSite.NAMU_WIKI; // 정보를 크롤링할 대상 사이트
}
