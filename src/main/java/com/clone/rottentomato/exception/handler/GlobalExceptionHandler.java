package com.clone.rottentomato.exception.handler;

import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.common.constant.CommonError;
import com.clone.rottentomato.exception.CommonException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * CommonException 발생 시 공통 에러 응답 처리
     */
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<CommonResponse> handleCommonException(CommonException e) {
        CommonResponse errorResponse = buildErrorResponse(e);
        return new ResponseEntity<>(errorResponse, HttpStatusCode.valueOf(e.getErrorStatus().getCode()));
    }

    /**
     * 모든 예외를 공통 에러 응답으로 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handleGenericException(Exception e) {
        CommonResponse errorResponse = buildErrorResponse(e);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 에러 공통 응답 변환 처리
     */
    private CommonResponse buildErrorResponse(Exception e) {
        // 추가 적으로 다른 에러를 만들어 글로벌로 처리 할거면 하단에 추가해주세요.
        if(e instanceof CommonException commonException) {
           return ofCommonException(e, commonException);
        }

        return ofOtherException(e);
    }

    /** CommonException -> 공통 응답 값 반환 */
    private CommonResponse ofCommonException(Exception e, CommonException commonException) {
        CommonResponse commonResponse = new CommonResponse();

        if (!Objects.isNull(commonException.getResultMap())) {
            commonResponse.setData(commonException.getResultMap());
        } else {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("errorCode", commonException.getErrorCode());
            resultMap.put("errorMessage", e.getMessage());
            commonResponse.setData(resultMap);
        }

        commonResponse.setErrorResponse(commonException.getErrorStatus());
        return commonResponse;
    }

    /** 그 외 Exception -> 공통 응답 값 반환 */
    private CommonResponse ofOtherException(Exception e) {
        CommonResponse commonResponse = new CommonResponse();

        // 그 외 오류는, UNKNOWN_ERROR 로 고정
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("errorCode", CommonError.UNKNOWN_ERROR.name());
        resultMap.put("errorMessage", String.format("[내부오류][%s]", e.getMessage()));
        commonResponse.setData(resultMap);
        commonResponse.setErrorResponse(CommonError.API_ERROR_OK);

        return commonResponse;
    }

}
