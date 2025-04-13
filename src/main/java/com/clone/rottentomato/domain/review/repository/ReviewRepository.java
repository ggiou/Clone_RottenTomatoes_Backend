package com.clone.rottentomato.domain.review.repository;


import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.review.component.dto.ReviewRatingDto;
import com.clone.rottentomato.domain.review.component.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.member = :member AND r.movie = :movie ORDER BY r.regDate DESC")
    Optional<Review> findTopByMemberAndMovieOrderByRegDateDesc(Member member, Movie movie);

    @Query("select r from Review r where r.review_id=:reviewId and r.member=:member")
    Optional<Review> findByIdAndMember(Long reviewId, Member member);

    @Query("select r from Review r where r.member.memberEmail=:memberEmail")
    Page<Review> findByAndMemberEmail(Pageable pageable, String memberEmail);

    // 영화 평점 계산을 위한 데이터 가져오기
    @Query("SELECT new com.clone.rottentomato.domain.review.component.dto.ReviewRatingDto(ifnull(count(r), 0L), ifnull(sum(r.rating), 0L))" +
            " FROM Movie m LEFT JOIN Review r ON m.id = r.movie.id WHERE m =:movie")
    ReviewRatingDto selectReviewRatingInfoByMovie(@Param("movie") Movie movie);

    @Query("select r from Review r where r.movie.id=:movieId")
    Page<Review> findByMovieId(Pageable pageable, Long movieId);
}
