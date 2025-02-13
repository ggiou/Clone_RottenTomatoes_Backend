package com.clone.rottentomato.common.constant;

import java.time.format.DateTimeFormatter;

public class CommonConst {
    /** 날짜 형식 */
    public static final String DEFAULT_DATE = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    // formatter
    public static DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE);
    public static DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME);

    // type
    public static String ALL = "ALL";   // 전체 의미
}
