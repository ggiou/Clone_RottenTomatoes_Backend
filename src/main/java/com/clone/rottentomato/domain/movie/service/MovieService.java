package com.clone.rottentomato.domain.movie.service;

import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.common.constant.CommonError;
import com.clone.rottentomato.common.constant.SortType;
import com.clone.rottentomato.crawling.constant.CrawlingSite;
import com.clone.rottentomato.crawling.service.WebDriverService;
import com.clone.rottentomato.crawling.service.WebElementService;
import com.clone.rottentomato.domain.movie.component.dto.*;
import com.clone.rottentomato.domain.movie.component.entity.*;
import com.clone.rottentomato.domain.movie.constant.MovieError;
import com.clone.rottentomato.domain.movie.repository.*;
import com.clone.rottentomato.domain.movie.repository.custom.*;
import com.clone.rottentomato.exception.CommonException;
import com.clone.rottentomato.exception.JpaException;
import com.clone.rottentomato.exception.MovieException;
import com.clone.rottentomato.util.UtilMap;
import com.clone.rottentomato.util.UtilNumber;
import com.clone.rottentomato.util.UtilString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.clone.rottentomato.domain.movie.constant.ProducerType.*;
import static com.clone.rottentomato.common.constant.CommonConst.DATA_NAME.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {
    // repository
    private final MovieRepository movieRepository;
    private final MovieCustomRepository movieCustomRepository;

    private final MovieDetailRepository movieDetailRepository;
    private final MovieDetailCustomRepository movieDetailCustomRepository;

    private final MovieTrailerRepository movieTrailerRepository;
    private final MovieTrailerCustomRepository movieTrailerCustomRepository;

    private final CategoryInfoRepository categoryInfoRepository;
    private final CategoryInfoCustomRepository categoryInfoCustomRepository;
    private final MovieCategoryRepository movieCategoryRepository;
    private final MovieCategoryCustomRepository movieCategoryCustomRepository;

    private final ProducerCustomRepository producerCustomRepository;
    private final ProducerRepository producerRepository;
    private final MovieProducerCustomRepository movieProducerCustomRepository;
    
    private final MovieRecommendRepository movieRecommendRepository;
    private final MovieRecommendCustomRepository movieRecommendCustomRepository;

    // service
    private final WebElementService webElementService;


    /** 영화 pk를 통해 특정 영화의 상세 정보 반환 */
    public CommonResponse getMovieInfo(Long movieId) {
        if(Objects.isNull(movieId) || movieId<=0) {
            throw new MovieException("요청 정보가 잘못되었습니다. 영화 pk가 없습니다.", MovieError.BAD_REQUEST_MOVIE_ID);
        }
        // 영화 기본 정보
        Optional<Movie> movie = movieRepository.findById(movieId);
        if (movie.isEmpty()) {
            return CommonResponse.fail("해당 영화 정보가 존재하지 않습니다.", UtilMap.makeMap(FIND_MAP_NAME));
        }
        // 영화 전체 정보 요소 값 검색
        Optional<MovieDetail> movieDetail = movieDetailRepository.findByMovieId(movieId);
        List<MovieTrailer> movieTrailers = movieTrailerRepository.findAllByMovieIdOrderByDisplayOrderAsc(movieId);
        List<CategoryInfo> movieCategories = categoryInfoRepository.findCategoryInfoForMovieId(movieId);

        MovieInfoDto movieInfoDto = MovieInfoDto.fromEntity(movie.get(), movieDetail.orElse(null), movieTrailers, movieCategories);
        return CommonResponse.success(String.format("[%s] 영화 상세 정보를 가져오는데 성공했습니다.", movie.get().getName()), UtilMap.makeMap(FIND_MAP_NAME, movieInfoDto));
    }

    /** 영화 정렬기준과 요창 page에 따라 영화 리스트 반환  */
    public List<MovieFindResponse> getMovieListBySort(List<MovieFindRequest> requestList){
        if(Objects.isNull(requestList) || requestList.isEmpty()) throw new MovieException("영화 리스트 반환 요청 값이 없습니다.", MovieError.BAD_REQUEST_MOVIE_LIST_FIND);

        List<MovieFindResponse> findResList = new ArrayList<>();
        for(MovieFindRequest request : requestList){
            // 정렬 기준으로 리스트 반환
            if(Objects.isNull(request) || SortType.find(request.getSortType()) == null) continue;
            Sort sort = request.getSort();
            Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize(), sort);
            // 카테고리별 영화 리스트 탐색
            Page<MovieDto> bySort = movieRepository.findAllMovies(pageable);
            if (!Objects.isNull(bySort) && !bySort.getContent().isEmpty()) {
                List<MovieDto> content = bySort.getContent();
                int totalCnt = movieRepository.countMovie();
                findResList.add(MovieFindResponse.of(request.getSortStr(), totalCnt, content));
            }
        }
        return findResList;
    }

    /** 영화 카테고리에 따라 영화 리스트 반환  */
    public List<MovieFindResponse> getMovieListByCategory(List<MovieFindRequest> requestList){
        if(Objects.isNull(requestList) || requestList.isEmpty()) throw new MovieException("카테고리별 영화 리스트 반환 요청 값이 없습니다.", MovieError.BAD_REQUEST_MOVIE_LIST_FIND);

        List<MovieFindResponse> findResList = new ArrayList<>();
        for(MovieFindRequest request : requestList) {
            if(Objects.isNull(request) || StringUtils.isBlank(request.getFindValue())) continue;
            Long pk = UtilNumber.returnLong(request.getFindValue());
            Optional<CategoryInfo> categoryInfo;
            if (!Objects.isNull(pk)) {
                categoryInfo = categoryInfoRepository.findById(pk);
            } else {
                categoryInfo = categoryInfoRepository.findByName(request.getFindValue());
            }

            // 해당 pk가 존재한다면
            if (categoryInfo.isPresent()) {
                CategoryInfo category = categoryInfo.get();
                Sort sort = request.getSort();
                Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize(), sort);

                // 카테고리별 영화 리스트 탐색
                List<MovieDto> byCategory = movieCustomRepository.findPageByCategory(category.getId(), pageable);
                if (!byCategory.isEmpty()) {
                    int totalCnt =  movieRepository.countMovieByCategory(category.getId());
                    findResList.add(MovieFindResponse.of(category.getName(), totalCnt, byCategory));
                }
            }
        }
        return findResList;
    }

    /** 특정 영화의 추천 영화 탐색 */
    public List<RecommendMovieDto> searchRecommendMovieListByMovieId(Long movieId, int size){
        // 1. 입력한 id의 영화가 존재하는 지 확인
        Optional<Movie> targetMovieOpt = movieRepository.findById(movieId);
        if(targetMovieOpt.isEmpty()) throw new MovieException("해당 id의 영화 정보가 없습니다.", MovieError.BAD_REQUEST_MOVIE_ID);
        Movie targetMovie = targetMovieOpt.get();

        // 2. db에 저장된 유효한 추천 영화가 존재하는지 확인  (추천 영화 수정일(마지막 탐색일)이 오늘이 아니라면, 유효하지 x)
        List<RecommendMovieDto> recommendMovie = movieRecommendRepository.findValidRecommendMovieByMovie(targetMovie.getId());
        // 3. 유효한 추천 영화가 존재하지 않다면, 추천 영화 알고리즘을 통해 저장
        if (recommendMovie.isEmpty()){
            // 3-1. 추천 알고리즘을 통해 탐색
            recommendMovie = calculationRecommendMovie(targetMovie);
            // 3-2. 탐색 정보를 db에 저장
            try {
                List<Movie> recommendMovieEntities = recommendMovie.stream().map(t->Movie.fromDto(t.getRecommendMovie())).toList();
                List<MovieRecommend> savedList = movieRecommendCustomRepository.saveOrUpdateRecommendMovie(targetMovie, recommendMovieEntities);
                // 저장된 개수가 추천영화 탐색한 개수가 동일하다면, 그대로 탐색결과 반환, 아니라면 저장된애를 응답 값으로 만들어 반환
                if(savedList.size() != recommendMovie.size()) {
                    recommendMovie = savedList.stream().map(t -> RecommendMovieDto.forRecommend(t.getRecommendMovie(), t.getRecommendRank())).toList();
                }
            }catch (Exception e){
                log.info("[saveOrUpdateRecommendMovie] 추천 영화를 저장하는 중 오류가 발생했습니다.");
                e.printStackTrace();
            }
        }
        return recommendMovie;
    }

    /** 특정 영화의 추천 영화 등수를 계산하는 함수 */
    private List<RecommendMovieDto> calculationRecommendMovie(Movie targetMovie){
        List<RecommendMovieDto> recommendMovies = new ArrayList<>();
        Map<Long, RecommendMovieDto> calculationMap = new HashMap<>();

        // 1. 해당 영화를 저장한 사람들이 저장한 영화 가져오기
        List<RecommendMovieDto> savedScore = findSavedMoviesByMembersWhoSavedThis(targetMovie);
        savedScore.forEach(t-> calculationMap.put(t.getRecommendMovie().getId(), RecommendMovieDto.makeNewObj(t)));

        // 2. 해당 영화를 저장한 사람들이 좋아요한 영화 가져오기
        List<RecommendMovieDto> likedScore = findLikedMoviesByMembersWhoLikedThis(targetMovie);
        // 이미 존재한다면 점수만 + 해서 반환하기
        likedScore.forEach(t-> calculationMap.compute(t.getRecommendMovie().getId(), (k, v)
                -> Objects.isNull(v) ? RecommendMovieDto.makeNewObj(t) : v.returnAddScore(t.getScore())));

        // 3. 해당 영화를 별점 준 사람들이, 해당 영화 별점보다 더 높거나 같은 점수를 준 영화 가져오기
        List<RecommendMovieDto> reviewedScore = findReviewedMoviesByMembersWhoReviewThis(targetMovie);
        reviewedScore.forEach(t-> calculationMap.compute(t.getRecommendMovie().getId(), (k, v)
                -> Objects.isNull(v) ? RecommendMovieDto.makeNewObj(t) : v.returnAddScore(t.getScore())));

        // 4. 해당 영화의 장르에 따라 추가 점수 판단하기
        // 4-1. 1, 2, 3을 통해 계산한 영화의 개수가 10(한 영화의 추천영화는 최대 10개까지 저장)보다 작다면, 장르가 겹치며 평점이 높은순으로 부족한 개수만큼 가져오기
        if(calculationMap.size() < 10){
            // 이미 추천 영화에 계산된 영화들은 제외
            List<Long> excludeList = calculationMap.values().stream().map(t->t.getRecommendMovie().getId()).toList();
            List<RecommendMovieDto> categoryScore = findMoviesByMovieCategoryInclude(targetMovie, excludeList, PageRequest.of(0, 10 - calculationMap.size()));
            categoryScore.forEach(t-> calculationMap.put(t.getRecommendMovie().getId(), RecommendMovieDto.makeNewObj(t)));
        }

        // 4-2. 해당 영화와 장르가 겹치는 만큼 점수 곱하기 (영화 장르에 비중 + ) -> 추후 너무 크다면 +로 변경
        // 계산된 추천영화들이 타겟 영화와 카테고리가 몇개 겹치는지 데이터 가져오기
        List<Long> recommendMovieIds = new ArrayList<>(calculationMap.keySet());
        List<RecommendMovieDto> categoryMatchScore = movieCategoryRepository.findMovieCategoryMatches(targetMovie.getId(), recommendMovieIds);
        // 카테고리가 매칭되는 개수를 곱해, 최종적인 점수를 set 한다. (기존 점수가 0일 수도 있으니 +1 후 곱하기)
        categoryMatchScore.forEach(t-> calculationMap.compute(t.getRecommendMovie().getId(), (k, v)
                -> Objects.isNull(v) ? RecommendMovieDto.makeNewObj(t) : v.returnAddScore(1L).returnMultiplyScore(t.getScore())));

        // 5. 추천 점수별로 높은순으로 10등까지 등급을 내려, 추천영화 판단
        AtomicInteger rank = new AtomicInteger(1);
        recommendMovies = calculationMap.values().stream()
                .sorted(Comparator.comparing(RecommendMovieDto::getScore).reversed())
                .limit(10) // 추천 영화 정보는 최대 10개로 지정한다.
                .peek(t->t.setRecommendRank(rank.getAndIncrement()))
                .map(t-> RecommendMovieDto.of(t.getRecommendMovie(), t.getRecommendRank(), t.getScore()))
                .collect(Collectors.toList());

        return recommendMovies;
    }

    private List<RecommendMovieDto> findSavedMoviesByMembersWhoSavedThis(Movie movie, Pageable pageable){
        return movieRepository.findSavedMoviesByMembersWhoSavedThis(movie, pageable);
    }

    // 해당 영화를 저장한 사람들이 저장한 다른 영화 개수 순위대로, (default 10)
    private List<RecommendMovieDto> findSavedMoviesByMembersWhoSavedThis(Movie movie){
        return findSavedMoviesByMembersWhoSavedThis(movie, PageRequest.of(0, 10));
    }

    private List<RecommendMovieDto> findLikedMoviesByMembersWhoLikedThis(Movie movie, Pageable pageable){
        return movieRepository.findLikedMoviesByMembersWhoLikedThis(movie, pageable);
    }

    // 해당 영화를 저장한 사람들이 저장한 다른 영화 개수 순위대로, (default 10)
    private List<RecommendMovieDto> findLikedMoviesByMembersWhoLikedThis(Movie movie){
        return findLikedMoviesByMembersWhoLikedThis(movie, PageRequest.of(0, 10));
    }

    private List<RecommendMovieDto> findReviewedMoviesByMembersWhoReviewThis(Movie movie, Pageable pageable){
        return movieRepository.findReviewedMoviesByMembersWhoReviewThis(movie, pageable);
    }

    // 해당 영화에 리뷰 별점을 남긴 사람들이 더 높은 별점을 남긴 다른 영화 개수 순위대로, (default 10)
    private List<RecommendMovieDto> findReviewedMoviesByMembersWhoReviewThis(Movie movie){
        return findReviewedMoviesByMembersWhoReviewThis(movie, PageRequest.of(0, 10));
    }

    // 해당 영화에 장르와 동일한 장르를 많이 가진 영화 * 평점 순위대로, (제외할 영화 리스트에 해당하면 x)
    private List<RecommendMovieDto> findMoviesByMovieCategoryInclude(Movie movie, List<Long> excludeList, Pageable pageable){
        return movieRepository.findMoviesByMovieCategoryInclude(movie, excludeList, pageable);
    }



    /** 검색 기능 - 입력값과 동일한 문자의 영화 정보 리스트(배우이름 포함) 검색 후 반환 */
    public SearchResponse searchMovieList(String searchValue, int pageNo, int pageSize){
        if(StringUtils.isBlank(searchValue)){
            throw new MovieException("검색 할 내용이 없습니다. 검색 내용을 입력해주세요.", MovieError.BAD_REQUEST_SEARCH_VALUE);
        }
        searchValue = searchValue.replaceAll(" ", StringUtils.EMPTY);
        SearchResponse searchResponse = SearchResponse.fromSearchValue(searchValue);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        int pageStartCnt = pageNo * pageSize;
        try {
            // 1-1. 검색 값이 포함되는 이름의 영화 제작진 탐색
            int actorCnt = producerRepository.countByName(ACTOR, searchValue);
            int directorCnt = producerRepository.countByName(DIRECTOR, searchValue);
            List<Producer> actorList = pageStartCnt >= actorCnt ? new ArrayList<>() : producerRepository.findByNameLikeWithPageable(ACTOR, searchValue, pageable).getContent();
            List<Producer> directorList = pageStartCnt >= directorCnt ? new ArrayList<>() :producerRepository.findByNameLikeWithPageable(DIRECTOR, searchValue, pageable).getContent();

            // 1-2. index 처리와 함께 응답값 세팅
            if(!actorList.isEmpty()) {
                searchResponse.setActors(IntStream.range(0, actorList.size()).mapToObj(i -> {
                    Producer actor = actorList.get(i); return ProducerDto.of(pageStartCnt + i + 1, actor);
                }).collect(Collectors.toList()));
            }
            if(!directorList.isEmpty()){
                searchResponse.setDirectors(IntStream.range(0, directorList.size()).mapToObj(i -> {
                    Producer director = directorList.get(i); return ProducerDto.of(pageStartCnt + i + 1, director);
                }).collect(Collectors.toList()));
            }
            searchResponse.setActorsTotalCnt(actorCnt);
            searchResponse.setDirectorsTotalCnt(directorCnt);
        }catch (Exception e){
            searchResponse.setFail(String.format("%s 문자열이 포함된 영화 제작진을 검색하는 중 오류가 발생했습니다.\n[Error] %s", searchValue, e.getMessage()));
            log.error("[searchMovieList.findProducer -> Error] " + e);
        }

        try {
            // 2-1. 검색 값이 포함되는 영화 정보 탐색
            int movieCnt = movieDetailRepository.countByNameContaining(searchValue);
            List<MovieDetail> movieSearch = pageStartCnt >= movieCnt ? new ArrayList<>() : movieDetailRepository.searchByNameContaining(searchValue, pageable).getContent();
            // 2-2. index 처리와 함께 응답값 세팅
            if (!movieSearch.isEmpty()) {
                searchResponse.setMovieInfos(IntStream.range(0, movieSearch.size()).mapToObj(i -> {
                    MovieDetail movieDetail = movieSearch.get(i); return SearchMovieInfo.of(pageStartCnt + i + 1, movieSearch.get(i).getMovie(), movieDetail.getActorNames(), movieDetail.getDirectorNames());
                }).collect(Collectors.toList()));
            }
            searchResponse.setMovieInfosTotalCnt(movieCnt);
        }catch (Exception e){
            searchResponse.setFail(String.format("%s 문자열이 포함된 영화를 검색하는 중 오류가 발생했습니다.\n[Error] %s", searchValue, e.getMessage()));
            log.error("[searchMovieList.findMovie -> Error] " + e);
        }
        return searchResponse;
    }



    /** 특정 영화 정보를 저장하는 총 process */
    public CommonResponse saveMovieProcess(List<MovieSaveRequest> request){
        // 0. 응답 값 세팅
        MovieSaveResponse processData = new MovieSaveResponse();    // 공통응답에 들어갈 영화 정보 저장 결과 객체
        MovieSaveResponse crawlingResponse;   // 크롤링 결과 객체
        MovieSaveResponse dbSaveResponse;     // db 저장 결과 객체

        // 1. request 를 통해 크롤링해 영화 정보를 가져온다.
        try {
            crawlingResponse = getMovieInfoByCrawling(request);
        }catch (Exception e){
            log.error("[getMovieInfoByCrawling] 크롤링 과정에 오류가 발생했습니다. {}", e.getMessage());
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
            return CommonResponse.fail("[일부] 영화 정보를 저장하는데 성공했습니다.", UtilMap.makeMap("saveData", processData));
        }
        return CommonResponse.success("[전체] 영화 정보를 저장하는데 성공했습니다.", UtilMap.makeMap("saveData", processData));
    }

    /** 각 사이트별로 크롤링해 영화 정보 반환*/
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

    /** 네이버 영화 검색 크롤링을 통한, 영화 정보 리스트 반환 */
    private MovieSaveResponse getMovieInfoOfNaverByCrawling(List<MovieSaveRequest> requests){
        // 0. 응답 값 세팅
        List<MovieInfoDto> successList = new ArrayList<>();
        List<MovieInfoDto> failList = new ArrayList<>();

        for(MovieSaveRequest crawlingReq : requests) {
            MovieInfoDto movieInfoDto = new MovieInfoDto();
            // 검색 결과의, 영화 정보 영역
            WebElement movieCrawlingDataElement;
            try {
                // 1. 데이터를 가져올 사이트 접속
                try {
                    // 1-1. 네이버 검색 결과 창 접속
                    getWebDriverSiteForMovieSave(crawlingReq, " 정보");
                    try {
                        // 1-2. 검색 결과 창이 유효한지 확인 (네이버 영화 영역이 존재해야, 영화 정보 탐색 가능)
                        movieCrawlingDataElement = webElementService.getByMultipleClassNames("sc_new", "cs_common_module", "case_empasis", "_au_movie_content_wrap");
                    } catch (NoSuchElementException | TimeoutException e) {
                        throw new MovieException("잘못된 요청입니다. 영화 검색 결과 값이 존재하지 않습니다.");
                    }
                } catch (MovieException e) {
                    // 검색 결과가 네이버 영화창이 없다면 잘못된 요청이 이라는 메세지로 안내
                    MovieDto failDto = MovieDto.fromResult(crawlingReq.getName(), false, "[CrawlingSiteGet] 유효한 네이버 크롤링 대상 사이트에 접근하는데 실패했습니다. [Error] " + e.getMessage());
                    movieInfoDto.setMovieDto(failDto);
                    failList.add(movieInfoDto);
                    continue; // 현재 영화 크롤링 스킵
                } catch (Exception e) {
                    // 사이트 접속에 실패하면 중단
                    MovieDto failDto = MovieDto.fromResult(crawlingReq.getName(), false, "[CrawlingSiteGet] 유효한 네이버 크롤링 대상 사이트에 접근하는데 실패했습니다. [Error] " + e.getMessage());
                    movieInfoDto.setMovieDto(failDto);
                    failList.add(movieInfoDto);
                    log.error("[MovieException] " + e.getMessage()); // 에러 메시지 로깅
                    continue;
                }

                // 2. 영화 정보를 찾기
                // 2-1. 네이버 영화 기본정보 탭
                // 영화 제목
                WebElement titleElement = webElementService.getByClassName(movieCrawlingDataElement, "area_text_title");
                String movieTitle = webElementService.getByClassName(titleElement, "_text").getText();
                // 개봉일자, 카테고리(영화 장르)
                WebElement basicInfo = webElementService.getByMultipleClassNames(movieCrawlingDataElement, "cm_info_box");
                List<WebElement> basicInfoElementList = webElementService.getListByClassName(basicInfo, "info_group");
                // 개봉일자의 경우 yyyy.mm.dd 로 넘어와 yyyy-mm-dd 로 변경되게 문자열 변경
                String releaseDate = webElementService.getByTagName(basicInfoElementList.get(0), "dd").getText().replaceAll("\\.", "-").replaceFirst("-$", StringUtils.EMPTY);
                String categoryStr = webElementService.getByTagName(basicInfoElementList.get(2), "dd").getText().replaceAll(" ", StringUtils.EMPTY);
                // 영화 줄거리
                WebElement storyElement = webElementService.getByMultipleClassNames(movieCrawlingDataElement, "intro_box", "_content ");
                String story = webElementService.getByClassName(storyElement, "_content_text").getText();

                // 2-2. 네이버 영화 출연/제작진 탭
                // 해당 탭으로 페이지 이동 (출연/제작진 탭으로 이동)
                getPage(CrawlingSite.NAVER.getMovieSearchFullUrl("영화 " + movieTitle + " 출연진"));
                try {
                    movieCrawlingDataElement = webElementService.getByMultipleClassNames("cm_content_wrap", "_actor_wrap");
                } catch (NoSuchElementException | TimeoutException e) {
                    MovieDto failDto = MovieDto.fromResult(crawlingReq.getName(), false, "[CrawlingSiteGet] 잘못된 요청입니다. 영화 제작진 정보가 존재하지 않습니다. [Error] " + e.getMessage());
                    movieInfoDto.setMovieDto(failDto);
                    failList.add(movieInfoDto);
                    continue;
                }
                // 감독 / 주연 배우 리스트 정보 요소 (이름이 길 경우 ... 으로 반환되 이를 방지하기 위해 , 이미지 설명값으로 가져온다. = 이미지 설명값은 네이버에서 해당 대상 이름으로 지정되어있음)
                List<WebElement> movieMakersElements = webElementService.getListByClassName(movieCrawlingDataElement, "cast_list");
                // 감독 이름
                List<WebElement> directorNameElements = webElementService.getListByTagName(movieMakersElements.get(0), "img");
                List<String> directorNames = directorNameElements.stream().map(t -> t.getAttribute("alt")).toList();
                // 주연 배우 이름
                List<WebElement> actorNameElements = webElementService.getListByTagName(movieMakersElements.get(1), "img");
                List<String> actorNames = actorNameElements.stream().map(t -> t.getAttribute("alt")).toList();

                // 2-3. 네이버 영화 포토 탭
                // 해당 탭으로 페이지 이동 (포토 탭으로 이동)
                getPage(CrawlingSite.NAVER.getMovieSearchFullUrl("영화 " + movieTitle + " 포토"));
                try {
                    // 2-3-1. 검색 결과 창이 유효한지 확인 (네이버 영화 포스터 정보 영역이 존재해야함)
                    movieCrawlingDataElement = webElementService.getByClassName("sec_movie_photo");
                } catch (NoSuchElementException | TimeoutException e) {
                    // 시리즈 물의 경우, 포토가 안나오는 경우가 있다. 한번더 이동
                    getPage(CrawlingSite.NAVER.getMovieSearchFullUrl("영화 " + movieTitle + "1 포토"));
                    try {
                        movieCrawlingDataElement = webElementService.getByClassName("sec_movie_photo");
                        movieTitle += "1";
                    } catch (NoSuchElementException | TimeoutException e2) {
                        MovieDto failDto = MovieDto.fromResult(crawlingReq.getName(), false, "[CrawlingSiteGet] 잘못된 요청입니다. 영화 포토 정보가 존재하지 않습니다. [Error] " + e.getMessage());
                        movieInfoDto.setMovieDto(failDto);
                        failList.add(movieInfoDto);
                        continue;
                    }
                }
                movieCrawlingDataElement = webElementService.getByClassName("sec_movie_photo");
                // 영화 포스터 정보 요소 (첫번째 포스터 url을 가져온다.)
                WebElement moviePosterElement = webElementService.getByMultipleClassNames(movieCrawlingDataElement, "area_card", "_image_base_poster");
                WebElement firstMoviePosterElement = webElementService.getByCssSelector(moviePosterElement, ".item._item[data-col='1']");
                String posterUrl = webElementService.getByTagName(firstMoviePosterElement, "img").getAttribute("src");

                // 3-1. 네이버 무비클립 탭 -> 네이버 무비의 경우 ui 노출이 이상해 유튜버에서 크롤링해오기로 변경
                getPage(CrawlingSite.NAVER.getMovieSearchFullUrl("영화 " + movieTitle + " 예고편"));
                try {
                    movieCrawlingDataElement = webElementService.getByMultipleClassNames("area_card", "_sec_movie_clip_trailer");
                } catch (NoSuchElementException | TimeoutException e) {
                    MovieDto failDto = MovieDto.fromResult(crawlingReq.getName(), false, "[CrawlingSiteGet] 잘못된 요청입니다. 영화 예고편 정보가 존재하지 않습니다. [Error] " + e.getMessage());
                    movieInfoDto.setMovieDto(failDto);
                    failList.add(movieInfoDto);
                    continue;
                }
                WebElement moreWrapElement = webElementService.getByClassName(movieCrawlingDataElement, "more_wrap");
                if (!Objects.isNull(moreWrapElement)) {
                    WebElement moreButton = webElementService.getByTagName(moreWrapElement, "a");
                    moreButton.click(); // 더보기 버튼 클릭
                    webElementService.waitForPageLoad();
                }
                WebElement trailerWrappedElement = webElementService.getByMultipleClassNames(movieCrawlingDataElement, "area_video_list", "_panel");
                List<WebElement> trailerElements = webElementService.getListByTagName(trailerWrappedElement, "li");
                // 유튜브에서 검색할 영화 예고편 이름 리스트
                List<String> movieTrailerNames = new ArrayList<>();
                for (WebElement trailerElement : trailerElements) {
                    WebElement areaInfoWrappedElement = webElementService.getByClassName(trailerElement, "area_info");
                    WebElement playInfo = webElementService.getByTagName(areaInfoWrappedElement, "a");
                    // 영화 트레일러 검색 이름
                    String playName = playInfo.getText();
                    movieTrailerNames.add(playName.replaceAll("[<>|/!]", StringUtils.EMPTY));
                }

                // 3-2. 유튜브 예고편 정보 크롤링 (네이버 무비 클립 제목을 통한 크롤링)
                List<MovieTrailerDto> movieTrailerDtos = new ArrayList<>();
                int disPlayOrder = 0;
                for (String trailerName : movieTrailerNames) {
                    String searchName = UtilString.isContain(trailerName, movieTitle) ? trailerName : String.format("%s %s", movieTitle, trailerName);
                    if (!UtilString.isContain(searchName, "예고편")) searchName += " 예고편";
                    try {
                        MovieTrailerDto movieTrailerDto = getMovieTrailerByYoutube(searchName, trailerName.replaceAll(movieTitle, StringUtils.EMPTY), ++disPlayOrder);
                        if (Objects.isNull(movieTrailerDto)) {
                            --disPlayOrder;
                            continue;
                        }
                        movieTrailerDtos.add(movieTrailerDto);

                    } catch (NoSuchElementException | TimeoutException e) {
                        MovieDto failDto = MovieDto.fromResult(crawlingReq.getName(), false, "[CrawlingSiteGet] 잘못된 요청입니다. 영화 예고편 정보가 존재하지 않습니다. [Error] " + e.getMessage());
                        movieInfoDto.setMovieDto(failDto);
                        failList.add(movieInfoDto);
                    }
                }

                // 네이버 영화 예고편과 동일한 이름의 예고편이 없다면, 영화이름 예고편 을 찾아 1개만 등록
                if (movieTrailerDtos.isEmpty()) {
                    MovieTrailerDto movieTrailerDto = getMovieTrailerByYoutube(String.format("%s %s", movieTitle, "예고편"), "예고편", ++disPlayOrder);
                    if (!Objects.isNull(movieTrailerDto)) movieTrailerDtos.add(movieTrailerDto);
                }

                // 가져온 정보를 기준으로 저장을 위한 dto 생성
                MovieDto movieDto = MovieDto.forSave(movieTitle, posterUrl, releaseDate);
                MovieDetailDto movieDetailDto = MovieDetailDto.forSave(story, UtilString.joinStrByDelimiter(actorNames, ","), UtilString.joinStrByDelimiter(directorNames, ","), actorNames, directorNames);
                List<CategoryInfoDto> categoryInfoDtos = Arrays.stream(categoryStr.split("[,/]")).map(CategoryInfoDto::forSave).toList();

                // 4. 네이버 크롤링이 성공했다면 만들어진 객체를, 성공리스트에 add
                movieInfoDto = new MovieInfoDto(movieDto, movieDetailDto, movieTrailerDtos, categoryInfoDtos, categoryStr.replaceAll("/", ","));
                successList.add(movieInfoDto);
            }catch (Exception e){
                // 특정 영화의 크롤링 실패시, 다음 영화 크롤링 진행을 위해, 실패 dto 설정
                MovieDto failDto = MovieDto.fromResult(crawlingReq.getName(), false, "[CrawlingSiteGet] 영화 정보를 크롤링하는 중 문제가 발생했습니다. [Error] " + e.getMessage());
                movieInfoDto.setMovieDto(failDto);
                failList.add(movieInfoDto);
                // 다음 크롤링 시 오류 발생 하지 않도록, 아예 드라이버 종료
                webElementService.quitDriver();
            }
        }
        // 데이터를 다 가져왔을 경우 드라이버 종료
        webElementService.quitDriver();
        return new MovieSaveResponse(successList, failList);
    }

    public MovieTrailerDto getMovieTrailerByYoutube(String searchName, String containName, int disPlayOrder) {
        getPage(CrawlingSite.YOUTUBE.getMovieSearchFullUrl(searchName));

        // 검색할 유튜브 영상 요소가 존재할때 까지 대기
        List<WebElement> youtubeTrailerList;
        try {
            youtubeTrailerList = webElementService.getListByCssSelectorWithWait("#dismissible.style-scope ytd-video-renderer");
        } catch (Exception e){
            log.error("[getMovieTrailerByYoutube] 유튜브 영상 크롤링 중 오류가 발생했습니다. {}\n error: {}", searchName, e.getMessage());
            youtubeTrailerList = webElementService.getListById("dismissible");
        }

        if (CollectionUtils.isEmpty(youtubeTrailerList)) return null;
        // 최소 10번 탐색을 위해, 재 탐색
        if (youtubeTrailerList.size() < 10) youtubeTrailerList = webElementService.getScrollToLoad(By.cssSelector("#dismissible.style-scope ytd-video-renderer"), 10, 5, 2000);
        int searchNum = 0;
        String[] containNameArr = containName.split(" ");

        // 검색 결과의 유튜브 영상 요소로 찾아 없다면(최대 5번 탐색) 해당 예고편은 제외
        for (WebElement trailer : youtubeTrailerList) {
            try {
                if (searchNum++ > 10) break; // (최대 10번 탐색)
                // 트레일러 url, 이름, 재생 시간
                WebElement trailerTitleElement = webElementService.getById(trailer, "video-title"); // 해당 이름 가져오는데 오류 발생?
                if (Objects.isNull(trailerTitleElement)) continue;
                String playName = Objects.requireNonNull(trailerTitleElement.getAttribute("title")).replaceAll("[<>|/!]", StringUtils.EMPTY);

                // 트레일러 이름이 포함되어 있지 않다면 다음 정보로 탐색
                String playStr = playName.replaceAll(" ", "");
                if (Arrays.stream(containNameArr).anyMatch(t -> !playStr.contains(t))) continue;

                String playUrl = trailerTitleElement.getAttribute("href");
                String playTime = UtilString.formatTime(webElementService.getByClassName(trailer, "badge-shape-wiz__text").getText());
                // 재생 시간이 숫자 형식이 아니라면 = 쇼츠라면 넘기기
                if (Objects.isNull(playTime)) continue;
                return MovieTrailerDto.forSave(disPlayOrder, playUrl, playName, playTime);
            }catch (Exception e){
                log.error("[getMovieTrailerByYoutube][{}] 유튜브 영화 예고편 정보를 탐색하는데 오류가 발생했습니다.\nerror : {}", searchName, e.getMessage());
            }
        }
        return null;
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
        this.webElementService.loadPage(url);
    }

    /** 영화 정보 전체를 db 에 저장 */
    @Transactional
    protected MovieSaveResponse saveMovieInfo(List<MovieInfoDto> saveMovieInfos){
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

            // 3. 영화 제작자 정보 저장
            List<Producer> actors = reqDto.getMovieDetailDto().getActorNameList().stream().map(t->Producer.of(t, ACTOR)).toList();
            List<Producer> directors = reqDto.getMovieDetailDto().getDirectorNameList().stream().map(t->Producer.of(t, DIRECTOR)).toList();
            List<Producer> producers = new ArrayList<>(actors);
            if(!producers.isEmpty() || !directors.isEmpty()) {
                try {
                    producers.addAll(directors);
                    producers = producerCustomRepository.saveProducer(producers);
                    final Movie targetMovie = movie;
                    List<MovieProducer> movieProducers = producers.stream().map(t->MovieProducer.of(targetMovie, t)).toList();
                    movieProducerCustomRepository.saveMovieProducer(movieProducers);
                } catch (Exception e) {
                    log.error(String.format("[%s] 영화 제작자 저장하는데 오류가 발생했습니다.\n[error] : %s \n[객체 정보] : %s ", movieName, e.getMessage(), UtilString.stringify(producers)));
                }
            }

            // 4. 영화 카테고리 정보 저장
            List<CategoryInfo> categoryInfoList = reqDto.getCategoryInfoDtoList().stream().map(CategoryInfo::fromDto).toList();
            try {
                // 4-1. 카테고리 정보가 없다면 저장해 정보 반환
                List<CategoryInfo> savedCategories = categoryInfoCustomRepository.saveCategoryInfoList(categoryInfoList);
                // 4-2. 영화 - 카테고리 연결 정보가 없다면 저장
                movieCategoryCustomRepository.saveMovieCategoryList(movie, savedCategories);
                // 카테고리 정보와, 연결 정보가 정상적으로 수행됬다면, 요청으로 들어온 값 전체 정보가 정상 저장되니, 요청값으로 세팅
                resDto.setCategoryInfoDtoList(reqDto.getCategoryInfoDtoList());
                resDto.setCategoryStr(reqDto.getCategoryStr());
            }catch (Exception e){
                log.error(String.format("[%s] 영화 카테고리 정보를 저장하는데 오류가 발생했습니다.\n[error] : %s \n[객체 정보] : %s ", movieName, e.getMessage(), categoryInfoList));
            }

            // 5. 영화 예고편 리스트 정보 저장
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

        // 6. 저장 여부 응답 값 반환
        return MovieSaveResponse.of(successList, failList);
    }
}
