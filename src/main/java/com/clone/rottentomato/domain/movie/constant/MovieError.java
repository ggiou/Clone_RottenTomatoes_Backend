package com.clone.rottentomato.domain.movie.constant;

import com.clone.rottentomato.common.constant.CustomError;
import com.clone.rottentomato.util.UtilString;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum MovieError implements CustomError {
    BAD_REQUEST_MOVIE_ID(400, "입력한 id는 유효한 영화 id가 아닙니다.", "BAD_REQUEST"),
    BAD_REQUEST_SEARCH_VALUE(400, "입력한 값은 검색이 불가합니다.", "BAD_REQUEST")
    ;

    private int code;
    private String msg;
    private String resCode;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String getResCode() {
        return UtilString.isNull(this.resCode, this.name());
    }

    public Map<String, Object> makeResultMap() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("errorCode", this.resCode);
        resultMap.put("errorMessage", this.msg);
        return resultMap;
    }
}
