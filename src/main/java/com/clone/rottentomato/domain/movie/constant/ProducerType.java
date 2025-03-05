package com.clone.rottentomato.domain.movie.constant;

import com.clone.rottentomato.common.constant.CommonError;
import com.clone.rottentomato.common.handler.EnumType;
import com.clone.rottentomato.exception.CommonException;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public enum ProducerType implements EnumType {
    ACTOR("ACTOR", "배우")
    , DIRECTOR("DIRECTOR", "감독")
    ;

    private final String type;
    private final String description;

    @Override
    public String getKey(){return this.name();}
    @Override
    public String getValue() {
        return this.type;
    }

    public static ProducerType find(ProducerType type){
        if(Objects.isNull(type)) throw new IllegalArgumentException("입력된 정보가 없습니다.");
        return find(type.name());
    }

    private static ProducerType find(final String site) {
        Optional<ProducerType> channelType = Arrays.stream(ProducerType.values())
                .filter(t -> t.name().equals(site))
                .findFirst();
        if(channelType.isPresent()) return channelType.get();
        throw new CommonException(getErrorMsg(), CommonError.BAD_REQUEST);

    }

    private static String getErrorMsg(){
        return "영화 제작자 유형이 잘못되었습니다.";
    }
}
