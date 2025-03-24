package com.clone.rottentomato.domain.review.service;


import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.common.component.dto.SortRequestDto;
import com.clone.rottentomato.domain.auth.component.UserDetailsImpl;
import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.repository.MovieRepository;
import com.clone.rottentomato.domain.mypage.component.dto.MypageReviewResponseDto;
import com.clone.rottentomato.domain.review.component.dto.ReviewRequestDto;
import com.clone.rottentomato.domain.review.component.dto.ReviewResponseDto;
import com.clone.rottentomato.domain.review.component.dto.SuccessResponse;
import com.clone.rottentomato.domain.review.component.entity.Review;
import com.clone.rottentomato.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;


    //  리뷰 작성
    public CommonResponse createReview(Long movieId, ReviewRequestDto reviewRequestDto, Member member) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new IllegalArgumentException("Movie not found")
        );
        Optional<Review> review = reviewRepository.findTopByMemberAndMovieOrderByRegDateDesc(member,movie);
        if(review.isPresent()) {
            log.info("------------------------------ 저장 예외 처리 -----------------------------------");
            log.info("review = {}",review);
            throw new IllegalArgumentException("이미 리뷰를 등록하셨습니다.");
        }
        movie.changeRating(movieRepository.selectAvgRatingByAddReviewRating(movie, reviewRequestDto.getRating()));
        Review saveReview = reviewRepository.save(Review.of(reviewRequestDto, member, movie));
        log.info("saveReview = {}",saveReview);
        log.info("------------------------------ 저장 성공 -----------------------------------");
        return CommonResponse.success("저장 성공",ReviewResponseDto.of(saveReview,member,movie));
    }


    //  리뷰 전체 조회
    @Transactional(readOnly = true)
    public ResponseEntity<List<MypageReviewResponseDto>> getReviews(Member member, int page, int size, SortRequestDto sortRequestDto) {
        Sort sort = getSortBySortType(sortRequestDto);
        Pageable pageable = PageRequest.of(page, size,sort);
        // 리뷰 리스트 조회
        Page<Review> reviewPage = reviewRepository.findByAndMemberEmail(pageable, member.getMemberEmail());
        if (reviewPage.isEmpty()) {
            log.info("------------------------------ Review 예외 처리 -----------------------------------");
            throw new IllegalArgumentException("작성된 리뷰가 없습니다.");
        }
        // 리뷰 정보를 DTO로 변환
        List<MypageReviewResponseDto> responseDtos = reviewPage.getContent().stream()
                .map(review -> {
                    Movie movie = movieRepository.findById(review.getMovie().getId())
                            .orElseThrow(() -> new IllegalArgumentException("해당 영화가 존재하지 않습니다."));
                    return MypageReviewResponseDto.from(review, member, movie);
                })
                .collect(Collectors.toList());
        log.info("responseDtos = {}",responseDtos);
        log.info("--------------------------- 전체 조회 성공 -----------------------------");
        return ResponseEntity.ok(responseDtos);
    }


    //  리뷰 상세 조회
    @Transactional(readOnly = true)
    public CommonResponse getreview(Long reviewId, Member member) {
        Optional<Review> reviewOptional = reviewRepository.findByIdAndMember(reviewId,member);
        if(reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            log.info("reviewOptional = {}",reviewOptional);
            log.info("--------------------------- 상세 조회 성공 -----------------------------");
            return CommonResponse.success("상세 조회 성공",ReviewResponseDto.of(review,member,review.getMovie()));
        }else{
            log.info("------------------------------ 게시글을 찾지 못함 -----------------------------------");
            throw new IllegalArgumentException("조회한 게시글이 없습니다.");
        }
    }


    //  리뷰 수정
    public CommonResponse updateReview(Long reviewId, UserDetailsImpl userDetails, ReviewRequestDto reviewRequestDto) {
        Review review = getReview(reviewId);
        Optional<Review> esxit = reviewRepository.findByIdAndMember(reviewId,userDetails.getMember());
        if(esxit.isEmpty()){
            log.info("------------------------------ 해당 사용자가 아니면 수정 예외 처리 -----------------------------------");
            throw new IllegalArgumentException("작성자만 수정 가능합니다.");
        }
        review.update(reviewRequestDto);
        if(!Objects.equals(reviewRequestDto.getRating(), esxit.get().getRating())){
            Movie reviewMovie = esxit.get().getMovie();
            reviewMovie.changeRating(movieRepository.selectAvgRatingByAddReviewRating(reviewMovie, reviewRequestDto.getRating()));
        }
        log.info("reviewRequestDto = {}",reviewRequestDto);
        log.info("--------------------------- 수정 성공 -----------------------------");
        return CommonResponse.success("수정 성공",ReviewResponseDto.update_from(review));
    }


    //  리뷰 삭제
    public CommonResponse deleteReview(Long reviewId, UserDetailsImpl userDetails) {
        Review review = getReview(reviewId);
        Optional<Review> esxit = reviewRepository.findByIdAndMember(reviewId,userDetails.getMember());
        if(esxit.isEmpty()){
            log.error("------------------------------ 해당 사용자가 아니면 삭제 예외 처리 -----------------------------------");
            throw new IllegalArgumentException("작성자만 삭제가 가능합니다.");
        }
        reviewRepository.delete(review);
        log.info("esxit = {}", esxit);
        log.info("--------------------------- 삭제 성공 -----------------------------");
        return CommonResponse.success("삭제 성공",SuccessResponse.of(HttpStatus.OK));
    }


//              ---         메서드             ---


    //  리뷰 ID값 조회 메서드
    private Review getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다.")
        );
        return review;
    }


    //  조회 Sort 메서드
    private Sort getSortBySortType(SortRequestDto sortRequest) {
        Sort.Direction direction = sortRequest.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC;

        return switch (sortRequest.getSortType()) {
            case ORDER -> Sort.by(direction, "최신순"); // 작성 날짜 기준 정렬
            case NAME -> Sort.by(direction, "제목순"); // 영화 제목 기준 정렬
            case HIGH -> Sort.by(direction, "높은순"); // 높은 평점 기준 정렬
            case LOW -> Sort.by(direction, "낮은순").descending(); // 낮은 평점 기준 정렬
            default -> Sort.by(Sort.Direction.DESC, "최신순"); // 기본적으로 최신순 정렬
        };
    }

}