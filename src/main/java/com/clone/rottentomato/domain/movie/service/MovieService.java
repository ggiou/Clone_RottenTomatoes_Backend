package com.clone.rottentomato.domain.movie.service;

import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.crawling.constant.CrawlingSite;
import com.clone.rottentomato.crawling.service.WebDriverService;
import com.clone.rottentomato.crawling.service.WebElementService;
import com.clone.rottentomato.domain.movie.component.dto.*;
import com.clone.rottentomato.domain.movie.component.entity.CategoryInfo;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.component.entity.MovieDetail;
import com.clone.rottentomato.domain.movie.component.entity.MovieTrailer;
import com.clone.rottentomato.domain.movie.repository.*;
import com.clone.rottentomato.exception.CommonException;
import com.clone.rottentomato.exception.JpaException;
import com.clone.rottentomato.util.UtilString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieDetailRepository movieDetailRepository;
    private final MovieTrailerRepository movieTrailerRepository;
    private final CategoryInfoRepository categoryInfoRepository;
    private final MovieCategoryRepository movieCategoryRepository;

    private final WebDriver webDriver = WebDriverService.getInstance().getChromeDriver();
    private final WebElementService webElementService = new WebElementService(webDriver);

    /** 특정 영화 정보를 저장하는 총 process */
    @Transactional(noRollbackFor = JpaException.class)
    public CommonResponse saveMovieProcess(List<MovieSaveRequest> request){
        // 0. 응답 값 세팅
        MovieSaveResponse processData = new MovieSaveResponse();    // 공통응답에 들어갈 영화 정보 저장 결과 객체
        MovieSaveResponse crawlingResponse;   // 크롤링 결과 객체
        MovieSaveResponse dbSaveResponse;     // db 저장 결과 객체

        // 1. request 를 통해 크롤링해 영화 정보를 가져온다.
        try {
            crawlingResponse = getMovieInfoByCrawling(request);
        }catch (Exception e){
            throw new CommonException(String.format("[saveMovieProcess.getMovieInfoByCrawling] 영화 정보를 크롤링 하는 과정에서 오류가 발생했습니다. \n %s",e.getMessage()));
        }

        // 2. 크롤링을 성공 여부에 따라 db에 저장한다.
        // 2-1. 크롤링이 실패할 경우, 응답값을 set 하고, 실패한 애들은 db 저장하지 않는다.
        processData.addSaveFailList(crawlingResponse.getSaveFailList());
        if(!CollectionUtils.isEmpty(crawlingResponse.getSaveSuccessList())){
            // 2-2. 크롤링을 성공한 영화 정보에 대해 db에 저장한다.
            try {
                dbSaveResponse = saveMovieInfo(crawlingResponse.getSaveSuccessList(), CrawlingSite.NAMU_WIKI);
                // 3. db 저장 성공 여부에 따라 응답값을 set 한다.
                processData.addResponse(dbSaveResponse);
            }catch (Exception e){
                throw JpaException.SaveFailException(String.format("[saveMovieProcess.saveMovieInfo] 영화 정보를 저장하는 과정에서 오류가 발생했습니다. \n %s",e.getMessage()), "movieRepository");
            }
        }

        //4. 최종적으로 공통 응답 값을 반환해준다. (한개라도 저장 실패 시, 실패 응답)
        if(!processData.getSaveFailList().isEmpty()){
            return CommonResponse.fail("[일부] 영화 정보를 저장하는데 성공했습니다.", processData);
        }
        return CommonResponse.success("[전체] 영화 정보를 저장하는데 성공했습니다.", processData);
    }

    private MovieSaveResponse getMovieInfoByCrawling(List<MovieSaveRequest> requests){
        // 0. 크롤링 응답 값 세팅
        MovieSaveResponse response = new MovieSaveResponse();

        // 1. request 를 정보를 크롤링할 대상 사이트 기준으로 나눈다.
        Map<CrawlingSite, List<MovieSaveRequest>> saveReqListBySite = requests.stream()
                .collect(Collectors.groupingBy(MovieSaveRequest :: getCrawlingSite,Collectors.toList()));

        List<MovieSaveRequest> namuWikiReqs = saveReqListBySite.get(CrawlingSite.NAMU_WIKI);

        // 2. 크롤링 대상 사이트 별 각 리스트가 존재하면 크롤링해 정보는 반환한다. (현재는 나무 위키만 존재, 추후 확장된다면, 타입별로 서비스 분리)
        if(!CollectionUtils.isEmpty(namuWikiReqs)){
            // 2-1 나무 위키에서 크롤링해 정보 반환
            MovieSaveResponse movieInfoByNamuWikiList = getMovieInfoOfNamuWikiByCrawling(namuWikiReqs);
            response.addResponse(movieInfoByNamuWikiList);
        }
        
        return response;
    }

    private MovieSaveResponse getMovieInfoOfNamuWikiByCrawling(List<MovieSaveRequest> requests){
        // 0. 응답 값 세팅
        List<MovieInfoDto> successList = new ArrayList<>();
        List<MovieInfoDto> failList = new ArrayList<>();

        for(MovieSaveRequest crawlingReq : requests) {
            // 1. 데이터를 가져올 나무 위키 사이트 접속
            getValidWebDriverSiteForMovieSave(crawlingReq);


            // 데이터를 다 가져온 창은 닫기
            webDriver.close();
        }

        return new MovieSaveResponse();
    }

    /** 영화 정보 저장을 위한 대상 사이트를 찾는 기능*/
    private void getValidWebDriverSiteForMovieSave(MovieSaveRequest request){
        boolean isValidSite = false;
        if (CrawlingSite.NAMU_WIKI.equals(request.getCrawlingSite())){
            if (StringUtils.isNotBlank(request.getSearchUrl())){
                // 사용자가 입력한 크롤링 사이트로 검색
                webDriver.get(request.getSearchUrl());
                return;
            }

            String searchUrl = CrawlingSite.NAMU_WIKI.getSearchFullUrl(request.getName());
        }
    }

    /** */
    private MovieSaveResponse saveMovieInfo(List<MovieInfoDto> saveMovieInfos, CrawlingSite site){
        // 0. 응답 값 기본 세팅
        List<MovieInfoDto> successList = new ArrayList<>();
        List<MovieInfoDto> failList = new ArrayList<>();

        for (MovieInfoDto reqDto : saveMovieInfos) {
            MovieInfoDto resDto = new MovieInfoDto();

            // 1. 영화 기본 정보 저장
            Movie movie = Movie.fromDto(reqDto.getMovieDto());
            try {
                resDto.setMovieDto(movieRepository.returnSaveOrUpdateMovie(movie));
            } catch (Exception e) {
                resDto.setMovieDto(MovieDto.fromResult(false, "영화 기본 정보를 저장하는 중 오류가 발생했습니다."));
                log.error(String.format("[%s] 영화 기본 정보를 저장하는데 오류가 발생했습니다.\n[error] : %s \n[객체 정보] : %s ", site.getKrName(), e.getMessage(),UtilString.stringify(movie)));
            }

            // 기본 영화 정보 저장에 실패한다면, 하위는 모두 영화 정보를 외래키로 가지므로, 중단
            if (!resDto.getMovieDto().isSuccess()){
                failList.add(resDto);
                continue;
            }

            // 2. 영화 상세 정보 저장
            MovieDetail movieDetail = MovieDetail.fromDto(reqDto.getMovieDetailDto(), movie);
            try {
                resDto.setMovieDetailDto(movieDetailRepository.returnSaveOrUpdateMovieDetail(movieDetail));
            }catch (Exception e){
                resDto.setMovieDetailDto(MovieDetailDto.fromEntity(movieDetail,false, "영화 상세 정보를 저장하는 중 오류가 발생했습니다."));
                log.error(String.format("[%s] 영화 상세 정보를 저장하는데 오류가 발생했습니다.\n[error] : %s \n[객체 정보] : %s ", site.getKrName(), e.getMessage(), UtilString.stringify(movieDetail)));
            }

            // 3. 영화 카테고리 정보 저장
            List<CategoryInfo> categoryInfoList = reqDto.getCategoryList().stream().map(CategoryInfo::fromDto).toList();
            try {
                // 3-1. 카테고리 정보가 없다면 저장해 정보 반환
                List<CategoryInfo> savedCategories = categoryInfoRepository.saveCategoryInfoList(categoryInfoList);
                // 3-2. 영화 - 카테고리 연결 정보가 없다면 저장
                movieCategoryRepository.saveMovieCategoryList(movie, savedCategories);
                // 카테고리 정보와, 연결 정보가 정상적으로 수행됬다면, 요청으로 들어온 값 전체 정보가 정상 저장되니, 요청값으로 세팅
                resDto.setCategoryList(reqDto.getCategoryList());
            }catch (Exception e){
                log.error(String.format("[%s] 영화 카테고리 정보를 저장하는데 오류가 발생했습니다.\n[error] : %s \n[객체 정보] : %s ", site.getKrName(), e.getMessage(), categoryInfoList));
            }

            // 4. 영화 예고편 리스트 정보 저장
            List<MovieTrailer> movieTrailers = reqDto.getMovieTrailerDtoList().stream().map(t->MovieTrailer.fromDto(t, movie)).toList();
            if(!CollectionUtils.isEmpty(movieTrailers)){
                List<MovieTrailerDto> saveResults = new ArrayList<>();
                for(MovieTrailer trailer : movieTrailers) {
                    try {
                        saveResults.add(movieTrailerRepository.returnSaveOrUpdateMovieTrailer(trailer));
                    } catch (Exception e) {
                        saveResults.add(MovieTrailerDto.fromEntity(trailer, false, "영화 예고편 정보를 저장하는 중 오류가 발생했습니다."));
                        log.error(String.format("[%s] 영화 예고편 정보를 저장하는데 오류가 발생했습니다.\n[error] : %s \n[객체 정보] : %s ", site.getKrName(), e.getMessage(), UtilString.stringify(trailer)));
                    }
                }
                resDto.setMovieTrailerDtoList(saveResults);
            }

            // 영화 데이터가 정상적으로 저장됬다면 성공 응답 세팅
            successList.add(resDto);
        }

        // 4. 저장 여부 응답 값 반환
        return MovieSaveResponse.of(successList, failList);
    }

}
