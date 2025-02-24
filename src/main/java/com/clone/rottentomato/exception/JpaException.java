package com.clone.rottentomato.exception;

import com.clone.rottentomato.common.constant.CustomError;
import com.clone.rottentomato.common.constant.JpaError;
import com.clone.rottentomato.util.UtilCommon;

import java.util.Map;

import static com.clone.rottentomato.common.constant.JpaError.*;

public class JpaException extends CommonException{
    public JpaException(String message) {
        super(message);
    }

    public JpaException(String message, CustomError errorStatus) {
        super(message, errorStatus);
    }

    public JpaException(String message, CustomError errorStatus, Map<String, Object> resultMap) {
        super(message, errorStatus, resultMap);
    }

    public static JpaException SaveFailException(String message, String jpaRepositoryName){
        Map<String, Object> resultMap = UtilCommon.makeResultMap(SAVE_ERROR.getResCode(), SAVE_ERROR.getMsg(jpaRepositoryName));
        return new JpaException(message, SAVE_ERROR, resultMap);
    }

    public static JpaException SaveFailException(String message){
        return new JpaException(message, SAVE_ERROR);
    }
}
