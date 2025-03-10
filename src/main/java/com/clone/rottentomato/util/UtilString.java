package com.clone.rottentomato.util;

import com.amazonaws.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 문자열 관련 유틸 클래스
 */
@Slf4j
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
            // URL 쿼리 부분만 인코딩
            int queryIndex = url.indexOf("?");
            if (queryIndex > -1) {
                String baseUrl = url.substring(0, queryIndex); // ? 이전 부분
                String query = url.substring(queryIndex + 1); // ? 이후 부분

                // 쿼리 부분을 다시 인코딩하여 URL을 재구성 (한글의 경우 url 체크를 위해 알맞게 encode / ex.빈공백 같은 경우 %20 으로 해줘야 함)
                String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
                String encodedUrl = baseUrl + "?" + encodedQuery;

                // URI가 유효한지 확인
                new URI(encodedUrl).toURL();
            } else {
                // 쿼리가 없다면 그냥 URL 확인
                new URI(url).toURL();
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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

    /** List 문자열을 구분자 기준으로 하나의 문자열로 반환 */
    public static String joinStrByDelimiter(List<String> stringList, String delimiter){
        if(Objects.isNull(stringList)) return StringUtils.EMPTY;
        stringList = stringList.stream().filter(StringUtils::isNotBlank).toList();
        if(stringList.isEmpty()) return StringUtils.EMPTY;
        return String.join(delimiter, stringList);
    }

    /** 문자열을 구분자 기준으로 하나의 List 반환 */
    public static List<String> makeListByDelimiter(String stringList, String delimiter){
        if(Objects.isNull(stringList)) return new ArrayList<>();
        stringList = stringList.replaceAll(" ", StringUtils.EMPTY);
        if(StringUtils.isBlank(stringList)) return new ArrayList<>();
        return List.of(stringList.split(delimiter));
    }
}
