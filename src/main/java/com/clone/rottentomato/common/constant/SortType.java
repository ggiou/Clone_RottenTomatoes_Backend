package com.clone.rottentomato.common.constant;

import com.clone.rottentomato.common.handler.EnumType;
import lombok.RequiredArgsConstructor;

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
}
