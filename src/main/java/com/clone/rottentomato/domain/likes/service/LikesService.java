package com.clone.rottentomato.domain.likes.service;


import com.clone.rottentomato.domain.likes.component.dto.LikesResponseDto;
import com.clone.rottentomato.domain.likes.component.entity.Likes;
import com.clone.rottentomato.domain.likes.repository.LikesRepository;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LikesService {

    private final LikesRepository likesRepository;
    private final MovieRepository movieRepository;


    //  좋아요
    public ResponseEntity<LikesResponseDto> ok(Long movieId) {
        Movie movie = getMovie(movieId);
        Optional<Likes> likes = likesRepository.findByMovie(movie);
        if(likes.isPresent()) {
            Likes findLikes = likes.get();
            likesRepository.delete(findLikes);
            int count = likesRepository.countByMovie(movie);
            return ResponseEntity.ok(LikesResponseDto.of(HttpStatus.OK,true,count));
        }
        Likes newLikes = Likes.of(movie);
        likesRepository.save(newLikes);
        int count = likesRepository.countByMovie(movie);
        return ResponseEntity.ok(LikesResponseDto.of(HttpStatus.OK,false,count));
    }



//          --              메서드             --
    private Movie getMovie(Long movieId) {
        return movieRepository.findById(movieId).orElseThrow(
                ()-> new IllegalArgumentException("게시물을 찾을 수 없습니다.")
        );
    }


}


