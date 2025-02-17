package com.clone.rottentomato.exception;

import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.common.constant.CommonError;
import com.clone.rottentomato.common.constant.CustomError;
import lombok.Getter;

import java.util.Map;

@Getter
public class CommonException extends RuntimeException {
    private String errorCode = CommonError.UNKNOWN_ERROR.getResCode();
    private CustomError errorStatus = CommonError.UNKNOWN_ERROR;
    private Map<String, Object> resultMap;

    public CommonException(String message) {
        super(message);
    }

    public CommonException(String message, String errorCode, CustomError errorStatus) {
        super(message);
        this.errorCode = errorCode;
        this.errorStatus = errorStatus;
    }

    public CommonException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CommonException(String message, Exception e) {
        super(message, e);
    }

    public CommonException(String message, Exception e, String errorCode) {
        super(message, e);
        this.errorCode = errorCode;
    }

    public CommonException(String message, Map<String, Object> resultMap) {
        super(message);
        this.resultMap = resultMap;
    }

    public CommonException(String message, CustomError errorStatus, Map<String, Object> resultMap) {
        super(message);
        this.errorStatus = errorStatus;
        this.resultMap = resultMap;
    }
}

