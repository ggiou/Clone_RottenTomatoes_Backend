package com.clone.rottentomato.domain.saved.repository;


import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.saved.component.entity.Saved;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavedRepository extends JpaRepository<Saved,Long> {

    @Query("select s from Saved s where s.movie=:movie and s.member =:member")
    Optional<Saved> findByMovieAndMember(Movie movie, Member member);

    int countByMovie(Movie movie);

    @Query("select s from Saved s where s.member.memberEmail=:email")
    Page<Saved> findByAndMemberEmail(Pageable pageable, String email);
}
