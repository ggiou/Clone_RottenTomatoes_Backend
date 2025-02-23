package com.clone.rottentomato.crawling.valid.validator;

import com.clone.rottentomato.crawling.constant.CrawlingSite;
import com.clone.rottentomato.crawling.valid.validation.ValidCrawlingSite;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class CrawlingSiteValidator implements ConstraintValidator<ValidCrawlingSite, CrawlingSite> {
    private String defaultMessage;

    @Override
    public void initialize(ValidCrawlingSite constraintAnnotation) {
        this.defaultMessage = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(CrawlingSite req, ConstraintValidatorContext context) {
        if(Objects.isNull(req)){
            context.buildConstraintViolationWithTemplate("유효한 크롤링 사이트가 존재하지 않습니다.").addConstraintViolation();
            return false;
        }
        try {
            CrawlingSite.find(req);
        }catch (Exception e){
            context.buildConstraintViolationWithTemplate(e.getMessage()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
