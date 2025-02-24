package com.clone.rottentomato.common.fomatter;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.clone.rottentomato.common.constant.CommonConst.DATE.DEFAULT_DATE_TIME;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@DateTimeFormat(pattern = DEFAULT_DATE_TIME)
//FIXME json으로 던지거나 받을 때 날짜형식 어떻게줘야하는지 확인 후 변경 필요 (기본 defaultDateTime 으로 우선 설정)
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_TIME, timezone="Asia/Seoul")
public @interface DefaultDateTimeFormat {
}
