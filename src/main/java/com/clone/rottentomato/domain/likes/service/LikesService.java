package com.clone.rottentomato.domain.likes.service;


import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.domain.likes.component.dto.LikesResponseDto;
import com.clone.rottentomato.domain.likes.component.dto.LikesStatusResponseDto;
import com.clone.rottentomato.domain.likes.component.entity.Likes;
import com.clone.rottentomato.domain.likes.repository.LikesRepository;
import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.member.repository.MemberRepository;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public CommonResponse ok(Long movieId, Member member, int isStatus) {
        Movie movie = getMovie(movieId);
        Optional<Likes> likes = likesRepository.findByIsStatusAndMovieAndMember(1, movie, member);
        Member findMember = getMember(member.getMemberId());
        log.info("findMember: {}", findMember);
        if(likes.isPresent()) {
            isStatus = 0;
            Likes findLikes = likes.get();
            likesRepository.delete(findLikes);
            int count = likesRepository.countByMovie(movie);
            log.info("findLikes : {}", findLikes);
            log.info("count : {}", count);
            log.info("----------------------- 취소하기 성공 --------------------");
            return CommonResponse.success("취소",LikesResponseDto.of(HttpStatus.OK,false,count,0));
        }
        isStatus = 1;
        Likes newLikes = Likes.of(movie, findMember, 1);
        likesRepository.save(newLikes);
        int count = likesRepository.countByMovie(movie);
        log.info("newLikes:{}", newLikes);
        log.info("count : {}", count);
        log.info("----------------------- 좋아요 성공 --------------------");
        return CommonResponse.success("좋아요",LikesResponseDto.of(HttpStatus.OK,true,count,isStatus));
    }


    //  저장하기 체크
    @Transactional(readOnly = true)
    public CommonResponse check(Member member, Long movieId) {
        Movie movie = getMovie(movieId);
        boolean liked = likesRepository.findByIsStatusAndMovieAndMember(1, movie, member).isPresent();
        return CommonResponse.success("좋아요 상태 조회 성공", new LikesStatusResponseDto(movieId, liked));
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


