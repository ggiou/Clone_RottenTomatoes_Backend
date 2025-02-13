package com.clone.rottentomato.common.component.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonResponse {

    //RESULT_CODE_SUCCESS: 성공
    private static Integer STATUS_CODE_SUCCESS = 200;
    private static String STATUS_SUCCESS = "success";
    private static String FAIL_STATUS  = "fail";
    private static final String ERROR_STATUS = "error";


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

    private void setBasicResponse(int statusCode, String status, String message){
        this.setStatusCode(statusCode);
        this.setStatus(status);
        this.setMessage(message);
    }


    public static CommonResponse Success(String msg){
        CommonResponse response = new CommonResponse();
        response.setSuccessResponse(msg);
        return response;
    }

    public static CommonResponse Success(String msg, Object data){
        CommonResponse response = new CommonResponse();
        response.setSuccessResponse(msg);
        response.setData(data);
        return response;
    }

    private void setSuccessResponse(String msg) {
        String resMsg = StringUtils.isBlank(msg) ? StringUtils.EMPTY : msg;
        this.setBasicResponse(STATUS_CODE_SUCCESS, STATUS_SUCCESS, resMsg);
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