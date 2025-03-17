package com.clone.rottentomato.crawling.constant;

import static com.clone.rottentomato.common.constant.CommonConst.USER_DIR;

public class CrawlingUrlConst {
    /** 기본 path */
    // ChromeDriver Path -> WINDOW = \, MAC&LINUX = /
    public static final String WINDOW_WEB_DRIVER_BASIC_PATH = USER_DIR + "\\src\\main\\resources\\driver\\selenium";
    public static final String MAC_LINUX_WEB_DRIVER_BASIC_PATH = USER_DIR + "/src/main/resources/driver/selenium";

    /** 크롤링에서 사용하는 사이트의 공통 url */
    public static class CRAWLING_SITE {
        public static final String NAVER_MOVIE_SEARCH_URL = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=";
        public static final String YOUTUBE_SEARCH_URL = "https://www.youtube.com/results?search_query=";

    }
}
