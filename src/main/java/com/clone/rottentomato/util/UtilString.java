package com.clone.rottentomato.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 문자열 관련 유틸 클래스
 */
public class UtilString {
    /** 문자열이 null 일 경우, 다른 문자열 반환 */
    public static String isNull(String str, String defaultStr) {
        return StringUtils.isEmpty(str) ? defaultStr : str;
    }
    /** 문자열이 null 일 경우, 빈 문자열 반환 */
    public static String isNull(String str) {
        return isNull(str, StringUtils.EMPTY);
    }

}
