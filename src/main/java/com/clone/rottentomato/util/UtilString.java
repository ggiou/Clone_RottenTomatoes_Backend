package com.clone.rottentomato.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

/**
 * 문자열 관련 유틸 클래스
 */
@RequiredArgsConstructor
public class UtilString {
    private static final UrlValidator urlValidator = new UrlValidator();

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
        return urlValidator.isValid(url);
    }
}
