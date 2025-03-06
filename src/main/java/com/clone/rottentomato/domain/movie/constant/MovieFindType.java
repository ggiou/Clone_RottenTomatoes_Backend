package com.clone.rottentomato.domain.movie.constant;

import com.clone.rottentomato.common.constant.CommonError;
import com.clone.rottentomato.common.handler.EnumType;
import com.clone.rottentomato.crawling.constant.CrawlingSite;
import com.clone.rottentomato.exception.CommonException;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

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

    public static MovieFindType find(MovieFindType type){
        if(Objects.isNull(type)) throw new IllegalArgumentException("입력된 정보가 없습니다.");
        return find(type.name());
    }

    private static MovieFindType find(final String site) {
        Optional<MovieFindType> channelType = Arrays.stream(MovieFindType.values())
                .filter(t -> t.name().equals(site))
                .findFirst();
        if(channelType.isPresent()) return channelType.get();
        throw new CommonException(getErrorMsg(), CommonError.BAD_REQUEST);

    }

    private static String getErrorMsg(){
        return "영화 검색 요청 타입이 잘못됬습니다. 유효한 타입은 "+ Arrays.toString(MovieFindType.values())+ " 입니다.";
    }
}
