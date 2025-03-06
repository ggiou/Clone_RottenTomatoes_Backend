package com.clone.rottentomato.util;

import java.util.HashMap;
import java.util.Map;

/** 공통으로 사용할 수 있는 util class
 * - 구분이 애매한 애들을 */
public class UtilCommon {
    /** 기본 에러 응답 값 반환 */
    public static Map<String, Object> makeResultMap(String errorCode, String errorMsg) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("errorCode", errorCode);
        resultMap.put("errorMessage", errorMsg);

        return resultMap;
    };
}
