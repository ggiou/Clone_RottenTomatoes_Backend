package com.clone.rottentomato.common.constant;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/** CommonResponse 에서 error 응답 일 경우, 에러 정보 data 객체 생성을 위해 필요한 interface
 *  <br>: 본인이 직접 data 생성하지 않고 도메인별로 공통 에러 data 생성할 경우 해당 interface implements 해서 사용 필요 */
public interface CustomError <T extends Enum<T>>{
    int getCode();
    String getMsg();
    String getResCode();
}
