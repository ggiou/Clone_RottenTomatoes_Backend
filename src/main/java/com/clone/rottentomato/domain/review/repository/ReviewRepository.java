package com.clone.rottentomato.domain.review.repository;


import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.review.component.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
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

}
