package com.clone.rottentomato.exception;

import com.clone.rottentomato.common.constant.CustomError;

public class MovieException extends CommonException{
    public MovieException(String message) {
        super(message);
    }

    public MovieException(String message, CustomError errorStatus) {
        super(message, errorStatus);
    }
}