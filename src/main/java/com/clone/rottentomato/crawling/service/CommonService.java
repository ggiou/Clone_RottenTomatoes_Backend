package com.clone.rottentomato.crawling.service;

import org.apache.commons.lang3.StringUtils;

import static com.clone.rottentomato.common.constant.CommonConst.OS.*;

/** 공통 사용할 수 있는 서비스 */
public class CommonService {

    /** 현재 서버가 실행 중인 os 반환 */
    public static String setRunningOs(){
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win")){
            return  OS_WIN;
        }

        if(os.contains("mac")){
            return OS_MAC;
        }
        if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            return OS_LINUX;
        }
        return StringUtils.EMPTY;
    }
}
