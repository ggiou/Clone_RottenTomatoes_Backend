package com.clone.rottentomato.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 문자열 관련 유틸 클래스
 */
@RequiredArgsConstructor
public class UtilString {
    /** 문자열 관련 사용할 ObjectMapper */
    public static ObjectMapper getObjectMapper() {
        ObjectMapper o = new ObjectMapper();
        o.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        o.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        o.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        o.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return o;
    }

    /** 문자열이 null 일 경우, 다른 문자열 반환 */
    public static String isNull(String str, String defaultStr) {
        return StringUtils.isEmpty(str) ? defaultStr : str;
    }
    /** 문자열이 null 일 경우, 빈 문자열 반환 */
    public static String isNull(String str) {
        return isNull(str, StringUtils.EMPTY);
    }

    /** 문자열이 적합한 url 형식인이 확인하는 함수 */
    public static boolean isUrlForm(String url) {
        if(StringUtils.isBlank(url)) return false;
        try {
            new URI(url).toURL(); // URI가 유효하면 true 반환
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Collections 객체를 JSON 문자열로 변환
     *
     * @param o Collections 객체 Map,List,Array
     * @return String
     */
    public static String stringify(Object o) {
        try {
            return getObjectMapper().writeValueAsString(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
