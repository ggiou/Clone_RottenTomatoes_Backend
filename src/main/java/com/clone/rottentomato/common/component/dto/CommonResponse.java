package com.clone.rottentomato.common.component.dto;

import com.clone.rottentomato.common.constant.CustomError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonResponse {

    //RESULT_CODE_SUCCESS: 성공
    private static Integer STATUS_CODE_SUCCESS = 200;
    private static String STATUS_SUCCESS = "success";
    private static String STATUS_FAIL  = "fail";
    private static final String STATUS_ERROR = "error";


    /**
     * 응답 코드 (HttpStatus 값 기준)
     * - STATUS_CODE_SUCCESS: 200 성공
     * - 실패 할 경우, 200 외 오류와 동일한 코드 입력
     */
    private Integer statusCode;

    /**
     * 응답 상태 값 (사용자 지정)
     * STATUS_SUCCESS = "success" - 성공
     * FAIL_STATUS  = "fail" - 실패   
     * ERROR_STATUS = "error" - 에러 발생
     */
    private String status;

    /**
     * 응답메시지
     */
    private String message;

    /**
     * 응답데이터
     */
    private Object data;

    /** 기본 함수 */
    private void setCommonResponse(int statusCode, String status, String message, Object data){
        this.setStatusCode(statusCode);
        this.setStatus(status);
        this.setMessage(message);
    }

    public void setCommonResponse(int statusCode, String status, String message){
        setCommonResponse(statusCode, status, message, null);
    }


    /** 성공 응답 관련 */
    public static CommonResponse success(String msg){
        CommonResponse response = new CommonResponse();
        response.setSuccessResponse(msg);
        return response;
    }

    public static CommonResponse success(String msg, Object data){
        CommonResponse response = new CommonResponse();
        response.setSuccessResponse(msg);
        response.setData(data);
        return response;
    }

    private void setSuccessResponse(String msg) {
        String resMsg = StringUtils.isBlank(msg) ? StringUtils.EMPTY : msg;
        this.setCommonResponse(STATUS_CODE_SUCCESS, STATUS_SUCCESS, resMsg);
    }

    /** 실패 응답 관련 */
    public static CommonResponse fail(String msg){
        CommonResponse response = new CommonResponse();
        response.setFailResponse(msg);
        return response;
    }

    public static CommonResponse fail(String msg, Object data){
        CommonResponse response = new CommonResponse();
        response.setFailResponse(msg);
        response.setData(data);
        return response;
    }

    private void setFailResponse(String msg) {
        String resMsg = StringUtils.isBlank(msg) ? StringUtils.EMPTY : msg;
        this.setCommonResponse(STATUS_CODE_SUCCESS, STATUS_FAIL, resMsg);
    }

    /** 에러 응답 관련 */
    public static CommonResponse error(String msg){
        return error(msg, 500, null);
    }

    public static CommonResponse error(String msg, int errCode){
        return error(msg, errCode, null);
    }

    public static CommonResponse error(String msg, int errorCode, Object data){
        CommonResponse response = new CommonResponse();
        response.setErrorResponse(errorCode, msg);
        response.setData(data);
        return response;
    }

    public static CommonResponse error(String msg, CustomError error){
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("errorCode", error.getResCode());
        errorData.put("errorMessage", error.getMsg());
        return error(msg, error.getCode(), errorData);
    }

    public void setErrorResponse(CustomError error){
        setErrorResponse(error.getCode(), error.getMsg());
    }

    private void setErrorResponse(int code, String msg){
        setCommonResponse(code, STATUS_ERROR, msg);
    }



}

/* 공통 응답 참조

- 성공
    {
    "statusCode": 200,
    "status": "success",
    "message": "전달할 문구",
    "data": {
                // 전달 할 데이터
           }
}

- 실패
    {
    "statusCode": 200,
    "status": "fail",
    "message": "전달할 문구",
    "data": {
                // 전달 할 데이터
           }
}

- 오류
    {
    "statusCode": 500,  // httpStatus 기준, 적합한 오류 상태의 value ( ex. HttpStatus.INTERNAL_SERVER_ERROR.value() )
    "status": "error",
    "message": "전달할 문구",
    "data": {
                // 전달 할 데이터 혹은 고정 error 값 사용 //TODO 이건 고정할지 의논 필요

                // 고정 error 값
                "errorMessage": "~~ 요청이 잘못되었습니다.",
                "errorCode": "INVALID_REQUEST"

           }
}

*/