package com.clone.rottentomato.common.constant;

import com.clone.rottentomato.util.UtilString;
import lombok.Getter;

/**
 * 사용자 CustomError
 * 공통으로 사용할 수 있는 에러만 정의
 */
@Getter
public enum CommonError implements CustomError {
    OK(200, "서버가 요청을 처리했습니다.", "OK"),
    NO_CONTENT(204, "서버가 요청을 처리했지만 응답 콘텐츠를 제공하지 않습니다.", "NO_CONTENT"),
    BAD_REQUEST(400, "서버가 요청의 구문을 인식하지 못했습니다.", "BAD_REQUEST"),
    FORBIDDEN(403, "서버가 요청을 거부하고 있습니다.", "FORBIDDEN"),
    NOT_FOUND(404, "요청한 URI path에 해당하는 리소스가 존재하지 않습니다.", "NOT_FOUND"),
    INTERNAL_SERVER_ERROR(500, "오류가 발생하여 요청을 수행할 수 없습니다.", "INTERNAL_SERVER_ERROR"),
    UNKNOWN_ERROR(500, "", "UNKNOWN_ERROR"),
    API_ERROR_OK(400, "오류 응답을 받았습니다. 응답 메시지를 확인 바랍니다.", "API_ERROR_OK"),
    FORMAL_ERROR(500, "유효한 형식이 아닙니다.", "INVALID_FORMAT"),
    DATE_FORMAL_ERROR(500, "유효한 날짜 형식이 아닙니다.", "INVALID_DATE_FORMAT")
    ;


    private int code;
    private String msg;
    private String resCode;

    CommonError(int code, String msg, String resCode) {
        this.code = code;
        this.msg = msg;
        this.resCode = resCode;
    }

    public String getResCode() {
        return UtilString.isNull(this.resCode, this.name());
    }

}