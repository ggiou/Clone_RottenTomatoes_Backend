package com.clone.rottentomato.domain.movie.valid.validator;

import com.clone.rottentomato.crawling.constant.CrawlingSite;
import com.clone.rottentomato.domain.movie.component.dto.MovieSaveRequest;
import com.clone.rottentomato.domain.movie.valid.validation.ValidMovieSaveRequest;
import com.clone.rottentomato.util.UtilJson;
import com.clone.rottentomato.util.UtilString;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
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

        // 영화 제목이나, 영화 url 둘 중 1개는 필수 (크롤링용)
        if(hasName) return true;
        if(hasSearchUrl){
            // 크롤링을 통해 데이터를 저장할 경우, 입력한 검색 url 이  대상 사이트의, 영화 검색 기본 url 이 포함되어 있어야한다.
            if(req.getSearchUrl().contains(req.getCrawlingSite().getMovieSearchUrl())){
                CrawlingSite site = req.getCrawlingSite();
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(String.format("\n입력하신 %s 는 잘못된 검색 요청 url 입니다.\n [%s]의 유효한 검색 url 형식은 \"%s\" 로 시작합니다.", req.getSearchUrl(), site.getKrName(), site.getMovieSearchUrl())).addConstraintViolation();

            }

            if(UtilString.isUrlForm(req.getSearchUrl())) return true;
            // 크롤링 대상 사이트가 적합한 url 이 아니라면 다른 문구로 안내
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(getErrorMessage(String.format("\n입력하신 %s 는 잘못된 검색 요청 url 입니다.", req.getSearchUrl()))).addConstraintViolation();
            return false;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(getErrorMessage(StringUtils.EMPTY)).addConstraintViolation();
        return false;
    }

    private String getErrorMessage(String addMsg) {
        return defaultMessage + addMsg + "\n유효한 영화의 (이름 / 이름 & 개봉연도 / 검색 url) 요청 값 을 넣어주세요";
    }

}
