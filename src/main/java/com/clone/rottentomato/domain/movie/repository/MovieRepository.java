package com.clone.rottentomato.domain.movie.repository;


import com.clone.rottentomato.domain.movie.component.dto.MovieDto;
import com.clone.rottentomato.domain.movie.component.dto.ProducerDto;
import com.clone.rottentomato.domain.movie.component.dto.SearchResponse;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.constant.ProducerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByName(String movieName);
    @Query("SELECT new com.clone.rottentomato.domain.movie.component.dto.MovieDto(m.id, m.name, m.rating, m.posterUrl, m.releaseDate) FROM Movie m")
    Page<MovieDto> findAllMovies(Pageable pageable);
    @Query("SELECT COUNT(m) FROM Movie m")
    int countMovie();
    @Query("SELECT COUNT(m) FROM Movie m INNER JOIN MovieCategory c ON m.id=c.movie.id WHERE c.categoryInfo.id =:categoryId")
    int countMovieByCategory(@Param("categoryId") Long categoryId);

 /*   @Query("SELECT new com.clone.rottentomato.domain.movie.component.dto.SearchMovieInfo(m, md.actorNames, md.directorNames) " +
            "FROM Movie m INNER JOIN MovieDetail md ON m.id = md.movie.id " +
            "WHERE m.name LIKE CONCAT('%', :name, '%') OR md.actorNames LIKE CONCAT('%', :name, '%') OR md.directorNames LIKE CONCAT('%', :name, '%') " +
            "ORDER BY CASE WHEN m.name LIKE CONCAT(:name, '%') OR md.actorNames LIKE CONCAT(:name, '%') OR md.directorNames LIKE CONCAT(:name, '%') THEN 1 " +
            "WHEN m.name LIKE CONCAT('%', :name, '%') OR md.actorNames LIKE CONCAT('%', :name, '%') OR md.directorNames LIKE CONCAT('%', :name, '%') THEN 2 " +
            "ELSE 3 END, m.rating DESC, m.releaseDate DESC")
    Page<SearchMovieInfo> searchByNameContaining(@Param("name") String movieName, Pageable pageable);

    @Query("SELECT COUNT(m) FROM Movie m INNER JOIN MovieDetail md ON m.id = md.movie.id " +
            "WHERE m.name LIKE CONCAT('%', :name, '%') OR md.actorNames LIKE CONCAT('%', :name, '%') OR md.directorNames LIKE CONCAT('%', :name, '%')")
    int countByNameContaining(@Param("name") String name);*/

}
