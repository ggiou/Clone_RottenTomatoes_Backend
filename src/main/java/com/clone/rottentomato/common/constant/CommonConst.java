package com.clone.rottentomato.common.constant;

import com.clone.rottentomato.crawling.service.CommonService;

import java.time.format.DateTimeFormatter;

/** 프로젝트 공통으로 사용할 수 있는 상수 */
public class CommonConst {
    // 현재 디렉터리 위치
    public final static String USER_DIR = System.getProperty("user.dir");

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


    /** 운영 체제 */
    public static class OS {
        public final static String OS_WIN = "Windows";
        public final static String OS_MAC = "macOs";
        public final static String OS_LINUX = "Linux";
        public final static String OS_WIN_DIR_PATH = "\\win";
        public final static String OS_MAC_DIR_PATH = "/mac";
        public final static String OS_LINUX_DIR_PATH = "/linux";

        // 현재 사용 중인 os
        public final static String RUNNING_OS = CommonService.setRunningOs();
    }
}
