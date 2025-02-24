package com.clone.rottentomato.common.constant;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Getter
public enum JpaError implements CustomError{
    SAVE_ERROR(500, "해당 데이터를 jpa 를 통해 db에 저장하는데 오류가 발생했습니다.", "DATA_SAVE_ERROR")
    ;

    private int code;
    private String msg;
    private String resCode;

    JpaError(int code, String msg, String resCode) {
        this.code = code;
        this.msg = msg;
        this.resCode = resCode;
    }

    public String getMsg(String repositoryName){
        if(StringUtils.isBlank(repositoryName)) return this.msg;
        return  String.format("[%s] %s", repositoryName, this.msg);
    }
}
