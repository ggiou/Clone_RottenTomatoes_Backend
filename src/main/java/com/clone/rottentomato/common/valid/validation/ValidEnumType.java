package com.clone.rottentomato.common.valid.validation;

import com.clone.rottentomato.common.handler.EnumType;
import com.clone.rottentomato.common.valid.validator.EnumTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EnumTypeValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnumType {
    String message() default "해당 값이 존재하지 않습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String fieldName() default StringUtils.EMPTY;
    Class<? extends Enum<? extends EnumType>> enumClass(); // EnumType 인터페이스를 구현한 enum만 허용
}