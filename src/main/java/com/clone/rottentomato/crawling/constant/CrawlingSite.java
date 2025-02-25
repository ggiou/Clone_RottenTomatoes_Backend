package com.clone.rottentomato.crawling.constant;

import com.clone.rottentomato.common.constant.CommonError;
import com.clone.rottentomato.common.handler.EnumType;
import com.clone.rottentomato.exception.CommonException;
import com.clone.rottentomato.util.UtilString;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static com.clone.rottentomato.crawling.constant.CrawlingUrlConst.CRAWLING_SITE.*;

@Getter
@RequiredArgsConstructor
public enum CrawlingSite implements EnumType {
    NAMU_WIKI("나무위키", NAMU_WIKI_SEARCH_URL)
    ;
    private final String krName;
    private final String searchUrl;


    @Override
    public String getKey() {
        return EnumType.super.getKey();
    }

    @Override
    public String getValue() {
        return krName;
    }

    public static CrawlingSite find(CrawlingSite type){
        if(Objects.isNull(type)) throw new IllegalArgumentException("입력된 정보가 없습니다.");
        return find(type.name());
    }

    private static CrawlingSite find(final String site) {
        Optional<CrawlingSite> channelType = Arrays.stream(CrawlingSite.values())
                .filter(t -> t.name().equals(site))
                .findFirst();
        if(channelType.isPresent()) return channelType.get();
        throw new CommonException(getErrorMsg(), CommonError.BAD_REQUEST);

    }

    /** 크롤링 사이트 + suffixUrl */
    public String getSearchFullUrl(String suffixUrl){
        if(StringUtils.isBlank(suffixUrl)) return StringUtils.EMPTY;
        if(suffixUrl.startsWith("/")) suffixUrl = suffixUrl.substring(1);
        String concatUrl = searchUrl + suffixUrl;
        if(UtilString.isUrlForm(concatUrl)) return concatUrl;
        throw new CommonException(getErrorMsg(), CommonError.FORMAL_ERROR);
    }

    private static String getErrorMsg(){
        return "입력한 크롤링할 사이트의 url이 잘못되었습니다.";
    }

    @JsonCreator
    public static CrawlingSite parsing(String inputValue) {
        return Arrays.stream(CrawlingSite.values()).filter(type
                -> type.name().equals(inputValue)).findFirst().orElse(null);
    }

}
