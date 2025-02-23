package com.clone.rottentomato.domain.likes.repository;


import com.clone.rottentomato.domain.likes.component.entity.Likes;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes,Long> {

    @Query("select l from Likes l where l.movie=:movie")
    Optional<Likes> findByMovie(Movie movie);

    int countByMovie(Movie movie);

}
