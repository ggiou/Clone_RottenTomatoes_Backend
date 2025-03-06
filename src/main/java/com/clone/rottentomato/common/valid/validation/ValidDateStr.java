package com.clone.rottentomato.common.valid.validation;

import com.clone.rottentomato.common.constant.CommonConst;
import com.clone.rottentomato.common.valid.validator.DateStrValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 날짜 형식으로 되어있는 문자열이, 원하는 format이 맞는지 확인하는 validation */
@Constraint(validatedBy = DateStrValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.TYPE }) // 클래스 레벨에서 적용
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateStr {
    String message() default "유효하지 않는 날짜 형식 문자입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String fieldName() default StringUtils.EMPTY;
    String dateFormat() default CommonConst.DATE.DEFAULT_DATE;
    boolean isNullable() default false;
}
