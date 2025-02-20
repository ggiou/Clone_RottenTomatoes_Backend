package com.clone.rottentomato.domain.likes.service;


import com.clone.rottentomato.domain.likes.component.dto.LikesResponseDto;
import com.clone.rottentomato.domain.likes.component.entity.Likes;
import com.clone.rottentomato.domain.likes.repository.LikesRepository;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikesService {

    private final LikesRepository likesRepository;
    private final MovieRepository movieRepository;


    //  지원하기
    public ResponseEntity<LikesResponseDto> ok(Long movieId) {
        Movie movie = getMovie(movieId);
        Optional<Likes> likes = likesRepository.findByMovie(movie);
        if(likes.isPresent()) {
            throw new IllegalArgumentException("이미 좋아요 버튼을 누르셨습니다.");
        }
        Likes newLikes = Likes.of(movie);
        likesRepository.save(newLikes);
        int count = likesRepository.countByMovie(movie);
        return ResponseEntity.ok(LikesResponseDto.of(HttpStatus.OK,true,count));
    }

    //  취소하기
    public ResponseEntity<LikesResponseDto> cancel(Long movieId) {
        Movie movie = getMovie(movieId);
        Optional<Likes> likes = likesRepository.findByMovie(movie);
        if(likes.isPresent()) {
            Likes findLikes = likes.get();
            likesRepository.delete(findLikes);
            int count = likesRepository.countByMovie(movie);
            return ResponseEntity.ok(LikesResponseDto.of(HttpStatus.OK,false,0));
        }else{
            throw new IllegalArgumentException("이미 취소되었습니다.");
        }
    }


//          --              메서드             --
    private Movie getMovie(Long movieId) {
        return movieRepository.findById(movieId).orElseThrow(
                ()-> new IllegalArgumentException("게시물을 찾을 수 없습니다.")
        );
    }


}


