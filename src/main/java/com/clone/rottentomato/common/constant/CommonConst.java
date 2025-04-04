package com.clone.rottentomato.common.constant;

import java.time.format.DateTimeFormatter;

/** 프로젝트 공통으로 사용할 수 있는 상수 */
public class CommonConst {
    /** 날짜 */
    public static class DATE {
        // 날짜 형식
        public static final String DEFAULT_DATE = "yyyy-MM-dd";
        public static final String DEFAULT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
        public static final String HOUR_MINUTE = "mm:ss";

        // formatter
        public static DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE);
        public static DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME);
    }


    /** CommonResponse.data 의 공통 이름 */
    public static class DATA_NAME{
        public final static String FIND_MAP_NAME = "findResponse";
    }
}
