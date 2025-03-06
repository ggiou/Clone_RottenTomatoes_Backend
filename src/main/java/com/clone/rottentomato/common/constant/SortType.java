package com.clone.rottentomato.common.constant;

import com.clone.rottentomato.common.handler.EnumType;
import com.clone.rottentomato.crawling.constant.CrawlingSite;
import com.clone.rottentomato.exception.CommonException;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public enum SortType implements EnumType {
    ORDER("ORDER", "번호 기준")
    , NAME("NAME", "이름 기준")
    , REGISTER("REGISTER", "등록일 기준")
    , RATING("RATING", "평점 기준")
    , RELEASE_DATE("RELEASE_DATE", "개봉일 기준")    // 영화에서 사용
    ;

    private final String sortType;  // 정렬명
    private final String description;   // 설명

    @Override
    public String getKey(){
        return this.name();
    }
    @Override
    public String getValue() {
        return null;
    }

    public static SortType find(SortType type){
        if(Objects.isNull(type)) throw new IllegalArgumentException("입력된 정보가 없습니다.");
        return find(type.name());
    }

    private static SortType find(final String site) {
        Optional<SortType> sort = Arrays.stream(SortType.values())
                .filter(t -> t.name().equals(site))
                .findFirst();
        // 정렬의 경우, 여러 곳에서 함께 사용하는 공통 클래스이므로 오류 대신, null 로 반환
        return sort.orElse(null);
    }
}
