package com.clone.rottentomato.domain.movie.service;

import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.common.constant.CommonError;
import com.clone.rottentomato.common.constant.CustomError;
import com.clone.rottentomato.crawling.constant.CrawlingSite;
import com.clone.rottentomato.crawling.service.WebDriverService;
import com.clone.rottentomato.crawling.service.WebElementService;
import com.clone.rottentomato.domain.movie.component.dto.*;
import com.clone.rottentomato.domain.movie.component.entity.CategoryInfo;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.component.entity.MovieDetail;
import com.clone.rottentomato.domain.movie.component.entity.MovieTrailer;
import com.clone.rottentomato.domain.movie.repository.*;
import com.clone.rottentomato.domain.movie.repository.custom.*;
import com.clone.rottentomato.exception.CommonException;
import com.clone.rottentomato.exception.JpaException;
import com.clone.rottentomato.exception.MovieException;
import com.clone.rottentomato.util.UtilString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieCustomRepository movieCustomRepository;
    private final MovieDetailCustomRepository movieDetailCustomRepository;
    private final MovieTrailerCustomRepository movieTrailerCustomRepository;
    private final MovieTrailerRepository movieTrailerRepository;
    private final CategoryInfoCustomRepository categoryInfoCustomRepository;
    private final MovieCategoryCustomRepository movieCategoryCustomRepository;

    private final WebDriverService webDriverService;
    private WebElementService webElementService;

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
                dbSaveResponse = saveMovieInfo(crawlingResponse.getSaveSuccessList());
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

        // 1-1. 네이버 검색을 통한 크롤링 (타겟)
        List<MovieSaveRequest> naverSearchReqs = saveReqListBySite.get(CrawlingSite.NAVER);

        // 2. 크롤링 대상 사이트 별 각 리스트가 존재하면 크롤링해 정보는 반환한다. (현재는 네이버만 존재, 추후 확장된다면, 타입별로 서비스 분리)
        if(!CollectionUtils.isEmpty(naverSearchReqs)){
            // 2-1 네이버 검색에서 영화 정보를 크롤링해 정보 반환
            MovieSaveResponse movieInfoByNaverList = getMovieInfoOfNaverByCrawling(naverSearchReqs);
            response.addResponse(movieInfoByNaverList);
        }
        
        return response;
    }

    private MovieSaveResponse getMovieInfoOfNaverByCrawling(List<MovieSaveRequest> requests){
        // 0. 응답 값 세팅
        List<MovieInfoDto> successList = new ArrayList<>();
        List<MovieInfoDto> failList = new ArrayList<>();

        for(MovieSaveRequest crawlingReq : requests) {
            MovieInfoDto movieInfoDto = new MovieInfoDto();
            // 네이버 검색 결과의, 영화 정보 영역
            WebElement naverDataElement;

            // 1. 데이터를 가져올 사이트 접속
            try {
                // 1-1. 네이버 검색 결과 창 접속
                getWebDriverSiteForMovieSave(crawlingReq, "정보");
                try {
                    // 1-2. 검색 결과 창이 유효한지 확인 (네이버 영화 영역이 존재해야, 영화 정보 탐색 가능)
                    naverDataElement = webElementService.getByMultipleClassNames("sc_new", "cs_common_module", "case_empasis", "_au_movie_content_wrap");
                }catch (NoSuchElementException|TimeoutException e){
                    throw new MovieException("잘못된 요청입니다. 영화 검색 결과 값이 존재하지 않습니다.");
                }
            } catch (MovieException e) {
                // 검색 결과가 네이버 영화창이 없다면 잘못된 요청이 이라는 메세지로 안내
                MovieDto failDto = MovieDto.fromResult(crawlingReq.getName(), false, "[CrawlingSiteGet] 유효한 네이버 크롤링 대상 사이트에 접근하는데 실패했습니다. [Error] " + e.getMessage());
                movieInfoDto.setMovieDto(failDto);
                failList.add(movieInfoDto);
                continue; // 현재 영화 크롤링 스킵
            } catch (Exception e){
                // 사이트 접속에 실패하면 중단
                MovieDto failDto = MovieDto.fromResult(crawlingReq.getName(),false, "[CrawlingSiteGet] 유효한 네이버 크롤링 대상 사이트에 접근하는데 실패했습니다. [Error] "+e.getMessage());
                movieInfoDto.setMovieDto(failDto);
                failList.add(movieInfoDto);
                log.error("[MovieException] " + e.getMessage()); // 에러 메시지 로깅
                continue;
            }

            // 2. 영화 정보를 찾기
            // 2-1. 네이버 영화 기본정보 탭
            // 영화 제목
            WebElement titleElement = webElementService.getByClassName(naverDataElement,"area_text_title");
            String movieTitle = webElementService.getByClassName(titleElement, "_text").getText();
            // 개봉일자, 카테고리(영화 장르)
            WebElement basicInfo = webElementService.getByMultipleClassNames(naverDataElement, "cm_info_box");
            List<WebElement> basicInfoElementList = webElementService.getListByClassName(basicInfo, "info_group");
            // 개봉일자의 경우 - - 로 바뀌게.. 문자열 변경
            String releaseDate = webElementService.getByTagName(basicInfoElementList.get(0), "dd").getText();
            String categoryStr = webElementService.getByTagName(basicInfoElementList.get(2), "dd").getText().replaceAll(" ", StringUtils.EMPTY);
            // 영화 줄거리
            WebElement storyElement = webElementService.getByMultipleClassNames(naverDataElement, "intro_box","_content ");
            String story = webElementService.getByClassName(storyElement, "_content_text").getText();

            // 2-2. 네이버 영화 출연/제작진 탭
            // 해당 탭으로 페이지 이동 (출연/제작진 탭으로 이동)
            getPage(CrawlingSite.NAVER.getMovieSearchFullUrl("영화"+movieTitle+"출연진"));
            naverDataElement = webElementService.getByMultipleClassNames("cm_content_wrap","_actor_wrap");
            // 감독 / 주연 배우 리스트 정보 요소
            List<WebElement> movieMakersElements = webElementService.getListByClassName(naverDataElement, "cast_list");
            // 감독 이름
            List<WebElement> directorNameElements = webElementService.getListByClassName(movieMakersElements.get(0), "name");
            List<String> directorNames = directorNameElements.stream().map(WebElement::getText).toList();
            // 주연 배우 이름
            List<WebElement> actorNameElements = webElementService.getListByClassName(movieMakersElements.get(1), "name");
            List<String> actorNames = actorNameElements.stream().map(WebElement::getText).toList();

            // 2-3. 네이버 영화 포토 탭
            // 해당 탭으로 페이지 이동 (포토 탭으로 이동)
            getPage(CrawlingSite.NAVER.getMovieSearchFullUrl("영화"+movieTitle+"포토"));
            naverDataElement = webElementService.getByClassName("sec_movie_photo");
            // 영화 포스터 정보 요소 (첫번째 포스터 url을 가져온다.)
            WebElement moviePosterElement = webElementService.getByMultipleClassNames(naverDataElement, "area_card","_image_base_poster");
            WebElement firstMoviePosterElement = webElementService.getByCssSelectore(moviePosterElement, ".item._item[data-col='1']");
            String posterUrl = webElementService.getByTagName(firstMoviePosterElement, "img").getAttribute("src");

            // 2-3. 네이버 무비클립 탭
            getPage(CrawlingSite.NAVER.getMovieSearchFullUrl("영화"+movieTitle+"예고편"));
            naverDataElement = webElementService.getByMultipleClassNames("area_card", "_sec_movie_clip_trailer");
            WebElement moreWrapElement = webElementService.getByClassName(naverDataElement, "more_wrap");
            if(!Objects.isNull(moreWrapElement)){
                WebElement moreButton = webElementService.getByTagName(moreWrapElement, "a");
                moreButton.click(); // 더보기 버튼 클릭
                webElementService.waitForPageLoad();
            }
            WebElement trailerWrappedElement = webElementService.getByMultipleClassNames(naverDataElement, "area_video_list", "_panel");
            List<WebElement> trailerElements = webElementService.getListByTagName(trailerWrappedElement, "li");
            int disPlayOrder = 0;
            // 영화 예고편 리스트
            List<MovieTrailerDto> movieTrailerDtos = new ArrayList<>();
            for(WebElement trailerElement : trailerElements) {
                WebElement areaInfoWrappedElement = webElementService.getByClassName(trailerElement, "area_info");
                WebElement playInfo = webElementService.getByTagName(areaInfoWrappedElement, "a");
                // 트레일러 url, 이름, 재생 시간
                String playUrl = playInfo.getAttribute("href");
                String playName = playInfo.getText();
                String playTime = webElementService.getByClassName(trailerElement, "play_time").getText();
                disPlayOrder += 1;
                movieTrailerDtos.add(MovieTrailerDto.forSave(disPlayOrder, playUrl, playName, playTime));
            }

            // 가져온 정보를 기준으로 저장을 위한 dto 생성
            MovieDto movieDto = MovieDto.forSave(movieTitle, posterUrl, releaseDate.replaceAll("\\.", "-").replaceFirst("-$", StringUtils.EMPTY));
            MovieDetailDto movieDetailDto = MovieDetailDto.forSave(story, actorNames, directorNames);
            List<CategoryInfoDto> categoryInfoDtos = Arrays.stream(categoryStr.split("[,/]")).map(CategoryInfoDto::forSave).toList();

            // 3. 네이버 크롤링이 성공했다면 만들어진 객체를, 성공리스트에 add
            movieInfoDto = new MovieInfoDto(movieDto, movieDetailDto, movieTrailerDtos, categoryInfoDtos, categoryStr.replaceAll("/", ","));
            successList.add(movieInfoDto);
        }
        // 데이터를 다 가져온 창은 닫기
        webDriverService.closePage();
        return new MovieSaveResponse(successList, failList);
    }

    /** 영화 정보 저장을 위한 대상 사이트를 찾는 기능
     * @param request 영화 요청 객체 정보
     * @param targetName 요청할 정보 타입 (ex. 기본정보, 무비 클립 .. 처럼 어떤 정보인지)
     * */

    private void getWebDriverSiteForMovieSave(MovieSaveRequest request, String targetName){
        String errorMsg = "크롤링 대상 사이트 접속에 실패했습니다.";
        if (CrawlingSite.NAVER.equals(request.getCrawlingSite())){
            if (StringUtils.isNotBlank(request.getSearchUrl())){
                boolean isValidUrl = true;
                if(!targetName.isEmpty()){
                    String[] parts = request.getSearchUrl().split("query=");
                    if (parts.length <= 1 || !parts[1].contains("영화") || !parts[1].contains(targetName.replaceAll(" ", StringUtils.EMPTY))){
                        errorMsg += String.format("%s\n요청 url 이 잘못됬습니다. 네이버 검색창에서 \"영화 영화이름 %s\" 로 입력해 검색한 결과의 url 을 입력해주세요.", errorMsg,targetName);
                        isValidUrl = false;
                    }
                }
                if (isValidUrl){
                    // 사용자가 입력한 크롤링 사이트 url 접속
                    getPage(request.getSearchUrl());
                    return;
                }
            }
            if(StringUtils.isNotBlank(request.getName())){
                // 사용자가 입력한 "영화" + "이름" 로 네이버 검색창 url 결과로 사이트 접속 (기본 정보)
                String searchUrl = CrawlingSite.NAVER.getMovieSearchFullUrl("영화 "+request.getName() + targetName);
                getPage(searchUrl);
                return;
            }
        }
        throw new MovieException(errorMsg, CommonError.INVALID_REQUEST);
    }


        /** 페이지 가져오기*/
    private void getPage(String url){
        webElementService = webDriverService.getPage(url, webElementService);
    }

    /** 영화 정보 전체를 db 에 저장 */
    private MovieSaveResponse saveMovieInfo(List<MovieInfoDto> saveMovieInfos){
        // 0. 응답 값 기본 세팅
        List<MovieInfoDto> successList = new ArrayList<>();
        List<MovieInfoDto> failList = new ArrayList<>();

        for (MovieInfoDto reqDto : saveMovieInfos) {
            MovieInfoDto resDto = new MovieInfoDto();

            // 1. 영화 기본 정보 저장
            Movie movie = Movie.fromDto(reqDto.getMovieDto());
            String movieName = UtilString.isNull(reqDto.getMovieDto().getName(), "ERROR");

            try {
                movie = movieCustomRepository.saveOrUpdateMovie(movie);
                resDto.setMovieDto(MovieDto.fromEntity(movie, true,"영화 정보 저장에 성공했습니다."));
            } catch (Exception e) {
                resDto.setMovieDto(MovieDto.fromEntity(movie, false, "영화 기본 정보를 저장하는 중 오류가 발생했습니다."));
                log.error(String.format("[%s] 영화 기본 정보를 저장하는데 오류가 발생했습니다.\n[error] : %s \n[객체 정보] : %s ", movieName, e.getMessage(),UtilString.stringify(movie)));
            }

            // 기본 영화 정보 저장에 실패한다면, 하위는 모두 영화 정보를 외래키로 가지므로, 중단
            if (!resDto.getMovieDto().isSuccess()){
                failList.add(resDto);
                continue;
            }

            // 2. 영화 상세 정보 저장
            MovieDetail movieDetail = MovieDetail.fromDto(reqDto.getMovieDetailDto(), movie);
            try {
                resDto.setMovieDetailDto(movieDetailCustomRepository.returnSaveOrUpdateMovieDetail(movieDetail));
            }catch (Exception e){
                resDto.setMovieDetailDto(MovieDetailDto.fromEntity(movieDetail,false, "영화 상세 정보를 저장하는 중 오류가 발생했습니다."));
                log.error(String.format("[%s] 영화 상세 정보를 저장하는데 오류가 발생했습니다.\n[error] : %s \n[객체 정보] : %s ", movieName, e.getMessage(), UtilString.stringify(movieDetail)));
            }

            // 3. 영화 카테고리 정보 저장
            List<CategoryInfo> categoryInfoList = reqDto.getCategoryList().stream().map(CategoryInfo::fromDto).toList();
            try {
                // 3-1. 카테고리 정보가 없다면 저장해 정보 반환
                List<CategoryInfo> savedCategories = categoryInfoCustomRepository.saveCategoryInfoList(categoryInfoList);
                // 3-2. 영화 - 카테고리 연결 정보가 없다면 저장
                movieCategoryCustomRepository.saveMovieCategoryList(movie, savedCategories);
                // 카테고리 정보와, 연결 정보가 정상적으로 수행됬다면, 요청으로 들어온 값 전체 정보가 정상 저장되니, 요청값으로 세팅
                resDto.setCategoryList(reqDto.getCategoryList());
                resDto.setCategoryStr(reqDto.getCategoryStr());
            }catch (Exception e){
                log.error(String.format("[%s] 영화 카테고리 정보를 저장하는데 오류가 발생했습니다.\n[error] : %s \n[객체 정보] : %s ", movieName, e.getMessage(), categoryInfoList));
            }

            // 4. 영화 예고편 리스트 정보 저장
            final Movie movieSaved = movie;
            List<MovieTrailer> movieTrailers = reqDto.getMovieTrailerDtoList().stream().map(t->MovieTrailer.fromDto(t, movieSaved)).toList();
            if(!CollectionUtils.isEmpty(movieTrailers)){
                List<MovieTrailerDto> saveResults = new ArrayList<>();
                for(MovieTrailer trailer : movieTrailers) {
                    try {
                        saveResults.add(movieTrailerCustomRepository.returnSaveOrUpdateMovieTrailer(trailer));
                    } catch (Exception e) {
                        saveResults.add(MovieTrailerDto.fromEntity(trailer, false, "영화 예고편 정보를 저장하는 중 오류가 발생했습니다."));
                        log.error(String.format("[%s] 영화 예고편 정보를 저장하는데 오류가 발생했습니다.\n[error] : %s \n[객체 정보] : %s ", movieName, e.getMessage(), UtilString.stringify(trailer)));
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
