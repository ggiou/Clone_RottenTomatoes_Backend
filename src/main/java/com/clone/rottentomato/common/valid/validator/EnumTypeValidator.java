package com.clone.rottentomato.common.valid.validator;

import com.clone.rottentomato.common.handler.EnumType;
import com.clone.rottentomato.common.valid.validation.ValidEnumType;
import com.clone.rottentomato.util.UtilString;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class EnumTypeValidator implements ConstraintValidator<ValidEnumType, Enum<?>> {

    private Class<? extends Enum<? extends EnumType>> enumClass;
    private String defaultMessage;
    private boolean isExistDefaultMessage = false;
    private String fieldName;
    private boolean isExistFieldName = false;

    @Override
    public void initialize(ValidEnumType constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
        if(!StringUtils.isBlank(defaultMessage) && !this.defaultMessage.equals("해당 값이 존재하지 않습니다.")) {
            this.defaultMessage = constraintAnnotation.message();
            this.isExistDefaultMessage = true;
        }
        this.fieldName = UtilString.isNull(String.format("[%s]", constraintAnnotation.fieldName()));
        this.isExistFieldName = !StringUtils.isBlank(fieldName);
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(getErrorMessage(" 해당 값이 빈 값으로 들어왔습니다.")).addConstraintViolation();
            return false;
        }

        // EnumType 인터페이스 구현 여부 확인
        if (!EnumType.class.isAssignableFrom(enumClass)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(getErrorMessage(" 유효하지 않은 EnumType 입니다.")).addConstraintViolation();
            return false;
        }

        try {
            // Enum 값 확인
            EnumType enumType = (EnumType) value;

            // EnumType의 key 값 검증
            if(enumType.getKey() != null){
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(getErrorMessage(" 유효하지 않은 EnumType 입니다.")).addConstraintViolation();
                return false;
            }
            return true;
        } catch (IllegalArgumentException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(getErrorMessage(" 유효하지 않은 구분자 값입니다.")).addConstraintViolation();
            return false;
        }
    }

    private String getErrorMessage(String msg){
        if(isExistDefaultMessage) msg = defaultMessage;
        return isExistFieldName ?  String.format("[%s] %s",fieldName , msg) : msg;
    }
}
