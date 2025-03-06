package com.clone.rottentomato.common.valid.validator;

import com.clone.rottentomato.common.valid.validation.ValidDateStr;
import com.clone.rottentomato.util.UtilDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class DateStrValidator implements ConstraintValidator<ValidDateStr, String> {
    private String defaultMessage;
    private String dateFormat;
    private String fieldName;
    private boolean isNullable;
    private boolean haveDefaultMsg;
    private boolean haveFieldName;

    @Override
    public void initialize(ValidDateStr constraintAnnotation) {
        this.defaultMessage = constraintAnnotation.message();
        this.dateFormat = constraintAnnotation.dateFormat();
        this.fieldName = constraintAnnotation.fieldName();
        this.isNullable = constraintAnnotation.isNullable();
        this.haveDefaultMsg = !StringUtils.isBlank(defaultMessage);
        this.haveFieldName = !StringUtils.isBlank(fieldName);
    }

    @Override
    public boolean isValid(String req, ConstraintValidatorContext context) {
        if(StringUtils.isBlank(req)){
            if(isNullable) return true;
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(getMsg("빈 문자열 입니다.")).addConstraintViolation();
            return false;
        }

        if(!UtilDate.isValidDate(req, dateFormat)){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(getMsg("유효한 날짜 형식 문자열이 아닙니다.")).addConstraintViolation();
            return false;
        }

        return true;
    }

    private String getMsg(String addMsg){
        if(haveDefaultMsg) addMsg = defaultMessage;
        return haveFieldName ? String.format("[%s] %s", fieldName, addMsg) : addMsg;
    }
}
