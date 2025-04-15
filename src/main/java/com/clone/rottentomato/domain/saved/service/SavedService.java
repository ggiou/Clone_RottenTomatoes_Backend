package com.clone.rottentomato.domain.saved.service;


import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.member.repository.MemberRepository;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.repository.MovieRepository;
import com.clone.rottentomato.domain.saved.component.dto.SavedResponseDto;
import com.clone.rottentomato.domain.saved.component.dto.SavedStatusResponseDto;
import com.clone.rottentomato.domain.saved.component.entity.Saved;
import com.clone.rottentomato.domain.saved.repository.SavedRepository;
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
public class SavedService {

    private final SavedRepository savedRepository;
    private final MovieRepository movieRepository;
    private final MemberRepository memberRepository;


    public CommonResponse save(Long movieId, Member member, int isStatus) {
        Movie movie = getMovie(movieId);
        Optional<Saved> save = savedRepository.findByIsStatusAndMovieAndMember(1,movie,member);
        Member findMember = getMember(member.getMemberId());
        log.info("findMember : {}", findMember);
        if(save.isPresent()) {
            isStatus = 0;
            Saved findSave = save.get();
            savedRepository.delete(findSave);
            int count = savedRepository.countByMovie(movie);
            log.info("findSave = {}",findSave);
            log.info("count = {}",count);
            log.info("----------------------- 취소하기 성공 --------------------");
            return CommonResponse.success("취소",SavedResponseDto.of(HttpStatus.OK,false,count,isStatus));
        }
        Saved newSave = Saved.of(movie,findMember,1);
        isStatus = 1;
        savedRepository.save(newSave);
        int count = savedRepository.countByMovie(movie);
        log.info("newSave = {}",newSave);
        log.info("count = {}",count);
        log.info("----------------------- 저장하기 성공 --------------------");
        return CommonResponse.success("저장",SavedResponseDto.of(HttpStatus.OK,true,count,isStatus));
    }


    //  좋아요 체크
    @Transactional(readOnly = true)
    public CommonResponse check(Member member, Long movieId) {
        Movie movie = getMovie(movieId);
        boolean saved = savedRepository.findByIsStatusAndMovieAndMember(1, movie, member).isPresent();
        return CommonResponse.success("좋아요 상태 조회 성공", new SavedStatusResponseDto(movieId, saved));
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


