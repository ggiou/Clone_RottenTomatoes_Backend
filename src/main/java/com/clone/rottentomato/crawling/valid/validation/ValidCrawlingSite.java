package com.clone.rottentomato.crawling.valid.validation;

import com.clone.rottentomato.crawling.valid.validator.CrawlingSiteValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CrawlingSiteValidator.class)
@Target({ ElementType.TYPE }) // 클래스 레벨에서 적용
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCrawlingSite {
    String message() default "유효하지 않는 크롤링 사이트 입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String fieldName() default StringUtils.EMPTY;
}
