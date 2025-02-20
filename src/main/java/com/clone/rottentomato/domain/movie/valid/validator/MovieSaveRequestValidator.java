package com.clone.rottentomato.domain.movie.valid.validator;

import com.clone.rottentomato.domain.movie.component.dto.MovieSaveRequest;
import com.clone.rottentomato.domain.movie.valid.validation.ValidMovieSaveRequest;
import com.clone.rottentomato.util.UtilString;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.URLValidator;

import java.util.Objects;

public class MovieSaveRequestValidator implements ConstraintValidator<ValidMovieSaveRequest, MovieSaveRequest> {
    private String defaultMessage;

    @Override
    public void initialize(ValidMovieSaveRequest constraintAnnotation) {
        this.defaultMessage = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(MovieSaveRequest req, ConstraintValidatorContext context) {
        if(Objects.isNull(req)){
            return false;
        }
        boolean hasName = StringUtils.isNotBlank(req.getName());
        boolean hasSearchUrl = StringUtils.isNotBlank(req.getSearchUrl());

        if(hasSearchUrl){
            UrlValidator urlValidator = new UrlValidator();
            hasSearchUrl = urlValidator.isValid(req.getSearchUrl());
        }

        // 영화 제목이나, 영화 url 둘 중 1개는 필수 (크롤링용)
        if(hasName || hasSearchUrl){
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(defaultMessage+ " [유효한 이름 | 이름 & 개봉연도 | 검색 url 을 넣어주세요]").addConstraintViolation();
        return false;
    }
}
