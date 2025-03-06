package com.clone.rottentomato.domain.movie.valid.validation;

import com.clone.rottentomato.domain.movie.valid.validator.MovieSaveRequestValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MovieSaveRequestValidator.class)
@Target({ ElementType.TYPE }) // 클래스 레벨에서 적용
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMovieSaveRequest {
    String message() default "영화 저장을 위한 요청 값이 잘못되었습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String fieldName() default StringUtils.EMPTY;
}
