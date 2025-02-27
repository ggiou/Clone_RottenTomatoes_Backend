package com.clone.rottentomato.domain.likes.repository;


import com.clone.rottentomato.domain.likes.component.entity.Likes;
import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes,Long> {

    @Query("select l from Likes l where l.movie=:movie and l.member =:member")
    Optional<Likes> findByMovieAndMember(Movie movie,Member member);

    int countByMovie(Movie movie);

    @Query("select l from Likes l where l.member.memberEmail=:email")
    Page<Likes> findByAndMemberEmail(Pageable pageable,String email);
}
