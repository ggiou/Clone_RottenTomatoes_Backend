package com.clone.rottentomato.domain.likes.service;


import com.clone.rottentomato.domain.likes.component.dto.LikesRequestDto;
import com.clone.rottentomato.domain.likes.component.dto.LikesResponseDto;
import com.clone.rottentomato.domain.likes.component.entity.Likes;
import com.clone.rottentomato.domain.likes.repository.LikesRepository;
import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;


    //  좋아요
    public ResponseEntity<LikesResponseDto> ok(Long movieId, Member member, LikesRequestDto requestDto) {
        int isStatus;
        Movie movie = getMovie(movieId);
        Optional<Likes> likes = likesRepository.findByMovieAndMember(movie,member);
        Member findMember = getMember(member.getMemberId());
        if(likes.isPresent()) {
            Likes findLikes = likes.get();
            isStatus = 0;           //  상태 값
            likesRepository.delete(findLikes);
            int count = likesRepository.countByMovie(movie);
            return ResponseEntity.ok(LikesResponseDto.of(HttpStatus.OK,false,count,isStatus));
        }
        Likes newLikes = Likes.of(movie,findMember, requestDto);
        isStatus = 1;           //  상태 값;
        likesRepository.save(newLikes);
        int count = likesRepository.countByMovie(movie);
        return ResponseEntity.ok(LikesResponseDto.of(HttpStatus.OK,true,count,isStatus));
    }


//          --              메서드             --
    private Movie getMovie(Long movieId) {
        return movieRepository.findById(movieId).orElseThrow(
                ()-> new IllegalArgumentException("게시물을 찾을 수 없습니다.")
        );
    }
    // MemberPost 정보 가져오기
    private Member getMember(Long memberId) {
        // 필요한 로직을 작성하여 MemberPost 객체를 반환합니다.
        // 예를 들어, memberPostRepository.findByMemberId(memberId)와 같은 방식으로 구현할 수 있습니다.
        return memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("회원 게시물을 찾을 수 없습니다.")
        );
    }


}


