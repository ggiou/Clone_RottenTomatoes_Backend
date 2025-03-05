package com.clone.rottentomato.domain.movie.constant;

import com.clone.rottentomato.common.handler.EnumType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MovieFindType implements EnumType {
    CATEGORY("CATEGORY", "카테고리(영화 장르) 기준")
    , ALL("ALL", "전체 영화 기준");

    private final String type;
    private final String description;

    @Override
    public String getKey() {
        return this.name();
    }

    @Override
    public String getValue() {
        return this.type;
    }
}
