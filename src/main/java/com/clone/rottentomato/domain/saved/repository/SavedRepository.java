package com.clone.rottentomato.domain.saved.repository;


import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.saved.component.entity.Saved;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavedRepository extends JpaRepository<Saved,Long> {

    @Query("SELECT l FROM Likes l WHERE l.isStatus = :isStatus AND l.movie = :movie AND l.member = :member")
    Optional<Saved> findByIsStatusAndMovieAndMember(@Param("isStatus") int isStatus, @Param("movie") Movie movie, @Param("member") Member member);

    int countByMovie(Movie movie);

    @Query("select s from Saved s where s.member.memberEmail=:email")
    Page<Saved> findByAndMemberEmail(Pageable pageable, String email);
}
